package com.ctecltd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import java.util.ArrayList;

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
				int returnVal = fc.showOpenDialog(fkwindow);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					inputFile = fc.getSelectedFile();
					inputTextField.setText(inputFile.toString());
					inputSelected = true;
					checkOK();
				}
			}
		});

		JPanel inputFileTypePanel = new JPanel();
		inputFileTypePanel.add(new JLabel("Input File Type: "));
		String[] inputFileTypeStrings = { "CSV From Etsy" };
		inputFileTypeComboBox = new JComboBox<>(inputFileTypeStrings);
		inputFileTypePanel.add(inputFileTypeComboBox);
		
		inputPanel.add(inputFileTypePanel,BorderLayout.NORTH);
		inputPanel.add(inputFilenamePanel,BorderLayout.SOUTH);		
		
		JPanel outputPanel = new JPanel();
		outputPanel.setLayout(new BorderLayout());
		outputPanel.add(new JLabel("Output File:"), BorderLayout.WEST);
		outputTextField = new JTextField();
//		outputTextField.setMinimumSize(new Dimension(200, 10));
		outputTextField.setText("output filename");
		outputPanel.add(outputTextField, BorderLayout.CENTER);
		outputBrowseButton = new JButton("Output...", createImageIcon("images/Save16.gif"));
		outputPanel.add(outputBrowseButton, BorderLayout.EAST);

		outputBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showSaveDialog(fkwindow);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					outputFile = fc.getSelectedFile();
					outputTextField.setText(outputFile.toString());
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
					JOptionPane.showMessageDialog(fkwindow, "Error: " + e.getMessage(), "CSV Conversion Error",
							JOptionPane.ERROR_MESSAGE);
				}
				JOptionPane.showMessageDialog(fkwindow, "Succesfully wrote output file: " + outputFile.getName(),
						"CSV Conversion Success", JOptionPane.PLAIN_MESSAGE);
			}
		});

		fkwindow.add(inputPanel, BorderLayout.NORTH);
		fkwindow.add(outputPanel, BorderLayout.CENTER);
		fkwindow.add(okPanel, BorderLayout.SOUTH);

		fkwindow.pack();
		fkwindow.setVisible(true);
	}

	protected static void writeOutputFile() throws BadItemNameException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

			writer.write("Sale Date,Item Name,Quantity,Description,Category,Company,Blank1,Blank2,Blank3,Item Total\n");

			for (InputItem item : inputItems) {
				String line = "";
				line += item.date + ",";
				String state = item.getState();
				line += state + ",";
				line += item.quantity + ",";
				String description = item.getDescription();
				line += description + ",";
				String category = item.getCategory();
				line += category + ",";
				String company = item.getCompany();
				line += company + ",";
				line += ",,,";
				line += item.total + "\n";
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
			final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
			for (final CSVRecord record : parser) {
				InputItem ii = new InputItem();
				ii.date = record.get("Sale Date");
				ii.name = record.get("Item Name");
				ii.quantity = record.get("Quantity");
				ii.total = record.get("Item Total");
				ii.variations = record.get("Variations");
				ii.sku = record.get("SKU");
				inputItems.add(ii);
			}
			parser.close();
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
