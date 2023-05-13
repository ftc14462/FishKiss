package com.ctecltd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.spi.FileTypeDetector;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class FishKissFileConverter {
	private static JTextField inputTextField;
	private static JButton inputBrowseButton;
	private static JTextField outputTextField;
	private static JButton outputBrowseButton;
	private static JButton okButton;
	private static JFileChooser fc;
	protected static File inputFile;
	protected static boolean inputSelected;
	protected static File outputFile;
	protected static boolean outputSelected;
	private static ArrayList<InputItem> inputItems;
	private static JComboBox inputFileTypeComboBox;
	private static final String[] InputFileTypeStrings = { "Etsy CSV", "Shopify CSV", "Amazon TXT" };
	private static final String[] MONTHS = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT",
			"NOV", "DEC" };

	public static void main(String[] args) {
		JFrame fkwindow = new JFrame("Fish Kiss File Converter");
		fkwindow.setPreferredSize(new Dimension(600, 200));
//		fkwindow.setLayout(new BorderLayout());

		fc = new JFileChooser();

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());

		JPanel inputFilenamePanel = new JPanel();
//		inputPanel.setBackground(Color.green);
		inputFilenamePanel.setLayout(new BorderLayout());
		inputFilenamePanel.add(new JLabel("Input File:"), BorderLayout.WEST);
		inputTextField = new JTextField();
//		inputTextField.setMinimumSize(new Dimension(300, 10));
		inputTextField.setText("input filename");
		inputFilenamePanel.add(inputTextField, BorderLayout.CENTER);
		inputBrowseButton = new JButton("Input...", createImageIcon("images/Open16.gif"));
		inputFilenamePanel.add(inputBrowseButton, BorderLayout.EAST);

		inputBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				int returnVal = fc.showOpenDialog(fkwindow);
//				if (returnVal == JFileChooser.APPROVE_OPTION) {
//					inputFile = fc.getSelectedFile();
//					inputTextField.setText(inputFile.toString());
//					inputSelected = true;
//					checkOK();
//				}
				FileDialog fd = new FileDialog(fkwindow, "Choose input", FileDialog.LOAD);
				fd.setVisible(true);
				String filename = fd.getFile();
				String dirString = fd.getDirectory();
				if (filename != null) {
					inputTextField.setText(dirString + filename);
					inputFile = new File(dirString + filename);
					inputSelected = true;
					autoSelectFiletype();
					autoSetOutputFilename();
					checkOK();
				}
			}
		});

		JPanel inputFileTypePanel = new JPanel();
		inputFileTypePanel.add(new JLabel("Input File Type: "));
		inputFileTypeComboBox = new JComboBox<>(InputFileTypeStrings);
		inputFileTypePanel.add(inputFileTypeComboBox);

		inputPanel.add(inputFileTypePanel, BorderLayout.CENTER);
		inputPanel.add(inputFilenamePanel, BorderLayout.NORTH);

		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new BorderLayout());
		outputPanel.add(new JLabel("Output File:"), BorderLayout.WEST);
		outputTextField = new JTextField();
//		outputTextField.setMinimumSize(new Dimension(200, 10));
		outputTextField.setText("output filename");
		outputPanel.add(outputTextField, BorderLayout.CENTER);
		outputBrowseButton = new JButton("Output...", createImageIcon("images/Save16.gif"));
		outputPanel.add(outputBrowseButton, BorderLayout.EAST);
		inputPanel.add(outputPanel, BorderLayout.SOUTH);

		outputBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				int returnVal = fc.showSaveDialog(fkwindow);
//				if (returnVal == JFileChooser.APPROVE_OPTION) {
//					outputFile = fc.getSelectedFile();
//					outputTextField.setText(outputFile.toString());
//					outputSelected = true;
//					checkOK();
//				}
				FileDialog fd = new FileDialog(fkwindow, "Choose output", FileDialog.SAVE);
				fd.setVisible(true);
				String filename = fd.getFile();
				if (filename == null) {
					return;
				}
				if (!filename.contains(".csv")) {
					filename = filename + ".csv";
				}
				String dirnameString = fd.getDirectory();
				if (filename != null) {
					outputTextField.setText(dirnameString + filename);
					outputFile = new File(dirnameString + filename);
					outputSelected = true;
					checkOK();
				}
			}

		});

		JPanel okPanel = new JPanel();
		okButton = new JButton("OK");
		okButton.setEnabled(false);
		okPanel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					readInputFile();
					writeOutputFile();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(fkwindow,
							"<html><body><p style='width: 200px;'>Error: " + e.getMessage()
									+ "Did you select the correct input file type?</p></body></html>",
							"CSV Conversion Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				JOptionPane.showMessageDialog(fkwindow, "Succesfully wrote output file: " + outputFile.getName(),
						"CSV Conversion Success", JOptionPane.PLAIN_MESSAGE);
			}
		});

		fkwindow.add(inputPanel, BorderLayout.NORTH);
//		fkwindow.add(outputPanel, BorderLayout.CENTER);
		fkwindow.add(okPanel, BorderLayout.SOUTH);

		fkwindow.pack();
		fkwindow.setVisible(true);
	}

	protected static void autoSetOutputFilename() {
		String filetype = inputFileTypeComboBox.getSelectedItem().toString();
		String filenameString = inputFile.getName();
		try {
			if (filetype.contains("Etsy")) {
				String date = filenameString.replace("EtsySoldOrderItems", "");
				date = date.split("\\.")[0];
//				date=date.split("\\-")[1] + "/1/" + date.split("\\-")[0];
//				Date d = DateFormat.getInstance().parse(date);
//				date = new Date(date).toGMTString();
				String monthString = date.split("\\-")[1];
				String yearString = date.split("\\-")[0];
				int month = Integer.parseInt(monthString);
				String mm = MONTHS[month - 1];
//				Date date2=new Date(Integer.parseInt(yearString),Integer.parseInt(monthString),1);
				String outfilenameString = mm + "_" + yearString + "_Etsy_Sales.csv";
				outputFile = new File(inputFile.getParent() + "\\" + outfilenameString);
				outputTextField.setText(outputFile.getAbsolutePath());
				outputSelected = true;
			} else if (filetype.contains("Amazon")) { // amazon
				outputFile = new File(inputFile.getParent() + "\\" + "amazon.csv");
				outputTextField.setText(outputFile.getAbsolutePath());
				outputSelected = false; // no clues about date from amazon filename
			} else { // shopify
				String date = filenameString.replace("sales_", "");
				date = date.split("_")[0];
//				date=date.split("\\-")[1] + "/1/" + date.split("\\-")[0];
//				Date d = DateFormat.getInstance().parse(date);
//				date = new Date(date).toGMTString();
				String monthString = date.split("\\-")[1];
				String yearString = date.split("\\-")[0];
				int month = Integer.parseInt(monthString);
				String mm = MONTHS[month - 1];
//				Date date2=new Date(Integer.parseInt(yearString),Integer.parseInt(monthString),1);
				String outfilenameString = mm + "_" + yearString + "_Shopify_Sales.csv";
				outputFile = new File(inputFile.getParent() + "\\" + outfilenameString);
				outputTextField.setText(outputFile.getAbsolutePath());
				outputSelected = true;
			}
		} catch (Exception e) {
			System.out.print("bad stuff");
		}
	}

	protected static void autoSelectFiletype() {
		if (inputFile == null) {
			return;
		}
		String filename = inputFile.getName();
		if (filename.toLowerCase().contains("etsy")) {
			inputFileTypeComboBox.setSelectedIndex(0);
		} else if (filename.toLowerCase().contains("txt")) {
			inputFileTypeComboBox.setSelectedIndex(2);
		} else {
			inputFileTypeComboBox.setSelectedIndex(1);
		}
	}

	protected static void writeOutputFile() throws BadItemNameException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

			writer.write(
					"Sale Date,Item Name,SKU,Quantity,Description,Category,Company,Discount Amount,Blank1,Blank2,Item Total,,Original Name\n");

			for (InputItem item : inputItems) {
				String line = "";
				line += item.getDate() + ",";
				String state = item.getState();
				line += state + ",";
				line += item.getSku() + ",";
				line += item.quantity + ",";
				String description = item.getDescription();
				line += description + ",";
				String category = item.getCategory();
				line += category + ",";
				String company = item.getCompany();
				line += company + ",";
				line += item.getDiscountAmount() + ",";
				line += ",,";
				line += item.total + ",,";
				line += "\"" + item.name + "\"\n";
				writer.write(line);
			}
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void readInputFile() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));

			inputItems = new ArrayList<InputItem>();

			String inputFileTypeString = inputFileTypeComboBox.getSelectedItem().toString();

			if (inputFileTypeString.contains("Etsy")) {
				final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
				for (final CSVRecord record : parser) {
					InputItem ii = new EtsyInputItem(record);
					inputItems.add(ii);
				}
				parser.close();
			} else if (inputFileTypeString.contains("Shopify")) {
				final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
				for (final CSVRecord record : parser) {
					InputItem ii = new ShopifyInputItem(record);
					inputItems.add(ii);
				}
				parser.close();
			} else {
				final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter('\t').withHeader());
				for (final CSVRecord record : parser) {
					InputItem ii = new AmazonInputItem(record);
					inputItems.add(ii);
				}
				parser.close();
			}

			reader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected static void checkOK() {
		if (inputSelected && outputSelected) {
			okButton.setEnabled(true);
		} else {
			okButton.setEnabled(false);
		}

	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = FishKissFileConverter.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
