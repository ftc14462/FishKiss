'''
Created on Dec 6, 2022

@author: scoot
'''

import csv
from _datetime import datetime
import matplotlib.pyplot as plt
import numpy as np


class EQ:

    def __init__(self, csv_text_row):
        self.time = csv_text_row[0]
        self.lat = float(csv_text_row[1])
        self.lon = float(csv_text_row[2])
        if self.lon < 0:
            self.lon = self.lon + 360
        self.depth = float(csv_text_row[3])
        self. mag = float(csv_text_row[4])
        
        # decode time stamp
        yy = int(self.time.split('-')[0])
        mm = int(self.time.split('-')[1])
        xx = self.time.split('-')[2]
        dd = int(xx.split('T')[0])
        xx = xx.split('T')[1]
        hh = int(xx.split(':')[0])
        mi = int(xx.split(':')[1])
        xx = xx.split(':')[2]
        ss = int(xx.split('.')[0])
        xx = xx.split('.')[1]
        ms = int(xx.split('Z')[0])
        
        self.datetime = datetime(year=yy, month=mm, day=dd, hour=hh, minute=mi, second=ss, microsecond=ms * 1000)
        
    def __lt__(self, obj):
        return ((self.datetime) < (obj.datetime))


eqs = list()

# filelist = [r'C:\Users\scoot\Downloads\solomon_islands_1981-2002_short_fat.csv',
#          r'C:\Users\scoot\Downloads\solomon_islands_2003-2022_short_fat.csv',]
filelist = [r'C:\Users\scoot\Downloads\solomon_islands_1980-1990_big.csv',
        r'C:\Users\scoot\Downloads\solomon_islands_1991-1995_big.csv',
        r'C:\Users\scoot\Downloads\solomon_islands_1996-2000_big.csv',
        r'C:\Users\scoot\Downloads\solomon_islands_2001-2005_big.csv',
        r'C:\Users\scoot\Downloads\solomon_islands_2006-2008_big.csv',
        r'C:\Users\scoot\Downloads\solomon_islands_2009-2011_big.csv',
        r'C:\Users\scoot\Downloads\solomon_islands_2012-2015_big.csv',
        r'C:\Users\scoot\Downloads\solomon_islands_2016-2018_big.csv',
        r'C:\Users\scoot\Downloads\solomon_islands_2019-2022_big.csv' ]

for ff in filelist:
    print(f'reading file {ff}')
    with open(ff, encoding='utf8') as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        line_count = 0    
        for row in csv_reader: 
            # print(f'row: {row}')
            if line_count == 0:
                # print(f'Column names are {", ".join(row)}')
                line_count += 1
            else:
                # print(f'\t{row[1]} lat {row[2]} lon')
                line_count += 1
                eq = EQ(row)            
                eqs.append(eq)            
                
        print(f'Processed {line_count} lines.')
    
tempdates = []
temps = []
with open(r'C:\Users\scoot\Downloads\sstoi.indices.txt') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    line_count = 0    
    for row in csv_reader: 
        if line_count == 0:
            print(f'Column names are {", ".join(row)}')
            line_count += 1
        else:
            yy = row[0].split()[0]
            mm = row[0].split()[1]
            tt = row[0].split()[8]
            tempdates.append(datetime(year=int(yy), month=int(mm), day=1, hour=0, minute=0, second=0, microsecond=0))
            temps.append(float(tt) - 27)
            
    print(f'Processed {line_count} lines.')

eqs.sort()

t = list()
m = list()
n = list()

lats = []
lons = []

prevTime = eqs[0].datetime

numEqs = 0
totMag = 0

for eq in eqs:
    # t.append(eq.datetime.timestamp())
    
    numEqs += 1
    totMag += eq.mag
    
    if  eq.lon > 195:
        continue    
    
    lats.append(eq.lat)
    lons.append(eq.lon)     
    
    # check if new month
    diff = eq.datetime - prevTime
    if diff.days > 30:
        t.append(eq.datetime)
        m.append(totMag / 4)
        n.append(numEqs)
        
        prevTime = eq.datetime
        numEqs = 0
        totMag = 0

m_smooth = m.copy()
n_smooth = n.copy()
for i in range(1, len(n) - 1):
    n_smooth[i] = sum(n[i - 1:i + 2]) / 3
    m_smooth[i] = sum(m[i - 1:i + 2]) / 3
    
t_smooth = temps.copy()
for i in range(1, len(temps) - 1):
    t_smooth[i] = sum(temps[i - 1:i + 2]) / 3
    
t_dir = temps.copy()
for i in range(1, len(temps) - 1):
    t_dir[i] = t_smooth[i + 1] - t_smooth[i - 1]
    
t_2dir= temps.copy()
for i in range(1, len(temps) - 1):
    t_2dir[i] = t_dir[i + 1] - t_dir[i - 1]
    
maxlon = max(lons)
minlon = min(lons)
maxlat = max(lats)
minlat = min(lats)

ncols = 3
nrows = 3

loninc = (maxlon - minlon) / ncols
latinc = (maxlat - minlat) / nrows

# bb the magnitudes
mags = np.zeros((ncols, nrows, len(t)))
first_time = eqs[0].datetime
for eq in eqs:
    
    if eq.lon > 195:
        continue     
    
    time_index = int((eq.datetime - first_time).days / 30)
    x_index = int((eq.lon - minlon) / loninc)
    y_index = int((eq.lat - minlat) / latinc)
    
    if x_index >= len(mags):
        x_index = len(mags) - 1
    if y_index >= len(mags[0]):
        y_index = len(mags[0]) - 1
    if time_index >= len(mags[0][0]):
        continue
        time_index = len(mags[0][0]) - 1
    
    mags[x_index, y_index, time_index] += eq.mag / 4

fig, (ax1, ax2) = plt.subplots(2, 1)

# ax1.plot(t, m, label='Moment (1/4)')

bb = 0
for r in range(0, nrows):
    for c in range(0, ncols):
        bb += 1
        # if bb < 4:
        #     continue
        totMag=sum(mags[c, r,:])/2
        if totMag > 200:
            ax1.plot(t, mags[c, r,:], label=f'Bin {bb} ({totMag:7.1f})')
       
# ax1.plot(t, n, label='EQ Count')
# ax1.plot(t, m_smooth, label='Moment smoothed')
# ax1.plot(t,n_smooth,label='Count smoothed')
ttt=np.array(t_dir)*np.array(t_2dir)
ax1b = ax1.twinx()
ax1b.plot(tempdates, temps, label='Temp', color='grey')
# ax1b.plot(tempdates, t_smooth, label='Temp Smoothed', color='lightgrey')
ax1b.plot(tempdates, t_dir, label='Temp Anomaly Derivative', color='black')
ax1b.plot(tempdates, ttt, label='Temp An. 2nd Der.', color='light brown')
# ax1.plot(tempdates,temps,label='Temp')
# ax1.set_yticks([20,25,30])

ax1.legend()
ax1b.legend(loc='lower right')
ax1.set_xlabel('Time')
ax1.set_ylabel('Total Per 30 Days')

ax1b.set_ylabel('Degrees C')
ax1.grid(True)
# fig.autofmt_xdate()

# drawgrid
bb=1
ngrids = ncols * nrows
for r in range(0, nrows):
    for c in range(0, ncols):
        x = minlon + c * loninc
        y = minlat + r * latinc
        ax2.plot([x, x], [y, y + latinc], color='lightgrey')
        ax2.plot([x, x + loninc], [y, y], color='lightgrey')
        s=sum(mags[c,r,:])/2
        ax2.annotate(f'{bb} ({s:7.1f})',(x+loninc/2, y+latinc/2))
        bb+=1

ax2.scatter(lons, lats, label='Eq')
ax2.set_xlabel('Lon (deg)')
ax2.set_ylabel('Lat (deg)')

fig.tight_layout(h_pad=0)
plt.show()

print(eqs)
