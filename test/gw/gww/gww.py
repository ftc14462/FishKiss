'''
Created on Dec 6, 2022

@author: scoot
'''

import csv
import os
from _datetime import datetime
import matplotlib.pyplot as plt
from cProfile import label


class EQ:

    def __init__(self, csv_text_row):
        self.time = csv_text_row[0]
        self.lat = float(csv_text_row[1])
        self.lon = float(csv_text_row[2])
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

filelist = [r'C:\Users\scoot\Downloads\solomon_islands_1981-2002_short_fat.csv',
         r'C:\Users\scoot\Downloads\solomon_islands_2003-2022_short_fat.csv',]
         # r'C:\Users\scoot\Downloads\solomon_islands_1991-1995_big.csv',
         # r'C:\Users\scoot\Downloads\solomon_islands_1996-2000_big.csv',
         # r'C:\Users\scoot\Downloads\solomon_islands_2001-2005_big.csv',
         # r'C:\Users\scoot\Downloads\solomon_islands_2006-2008_big.csv',
         # r'C:\Users\scoot\Downloads\solomon_islands_2009-2011_big.csv',
         # r'C:\Users\scoot\Downloads\solomon_islands_2012-2015_big.csv',
         # r'C:\Users\scoot\Downloads\solomon_islands_2016-2018_big.csv',
         # r'C:\Users\scoot\Downloads\solomon_islands_2019-2022_big.csv' ]

for ff in filelist:
    print(f'reading file {ff}')
    with open(ff, encoding='utf8') as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        line_count = 0    
        for row in csv_reader: 
            print(f'row: {row}')
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
            temps.append(float(tt))
            
    print(f'Processed {line_count} lines.')

eqs.sort()

t = list()
m = list()
n = list()

prevTime = eqs[0].datetime

numEqs = 0
totMag = 0

for eq in eqs:
    # t.append(eq.datetime.timestamp())
    
    numEqs += 1
    totMag += eq.mag
    
    # check if new month
    diff = eq.datetime - prevTime
    if diff.days > 30:
        t.append(eq.datetime)
        m.append(totMag / 4)
        n.append(numEqs)
        
        prevTime = eq.datetime
        numEqs = 0
        totMag = 0

fig, ax1 = plt.subplots(1, 1)

ax1.plot(t, m, label='Moment (1/4)')
ax1.plot(t, n, label='EQ Count')
ax2 = ax1.twinx()
ax2.plot(tempdates, temps, label='Temp', color='black')
# ax1.plot(tempdates,temps,label='Temp')
# ax1.set_yticks([20,25,30])

ax1.legend()
ax2.legend(loc='lower right')
ax1.set_xlabel('Time')
ax1.set_ylabel('Total Per 30 Days')
ax2.set_ylabel('Degrees C')
ax1.grid(True)
plt.gcf().autofmt_xdate()
plt.show()

print(eqs)
