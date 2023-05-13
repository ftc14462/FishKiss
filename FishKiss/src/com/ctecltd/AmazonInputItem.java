package com.ctecltd;

import org.apache.commons.csv.CSVRecord;

public class AmazonInputItem extends InputItem {

	public AmazonInputItem(CSVRecord record) {
		this.date = record.get("purchase-date");
		this.name = record.get("product-name");
		this.quantity = record.get("quantity");
		this.total = record.get("item-price");
		this.variations = "N/A";
		this.sku = record.get("sku");
		this.discountAmount = record.get("item-promotion-discount");

		keyWord = name;
	}

	public String getDescription() throws BadItemNameException {
		if (name == null) {
			return "bad item name";
		}
		// break name up so only initial statement remains
		keyWord = name.split("\\(")[0];
		keyWord = keyWord.split(",")[0];
		keyWord = keyWord.split("\\.")[0];
		keyWord = keyWord.trim();
		// last word of initial statement is keyword
		keyWord = keyWord.split(" ")[keyWord.split(" ").length - 1];
		return super.getDescription();
	}

	public String getDate() {
		if (date == null || !date.contains("T")) {
			return "bad date format";
		}
		date = date.split("T")[0];
		return date;
	}

	public boolean isArtItem() {
		if (name == null)
			return false;
		return (name.contains("Poster") || name.contains("Print") || name.contains("Canvas") || name.contains("Card"));
	}

	public String getArtDescription() {
		String descriptionString = "unkown art type";
		if (name == null) {
			return descriptionString;
		}
		String sizeString = getPosterSize();
		if (name.contains("Poster")) {
			descriptionString = sizeString + " Poster";
		} else if (name.contains("Print")) {
			descriptionString = sizeString;
		} else if (name.contains("Canvas")) {
			descriptionString = sizeString + " Canvas";
		} else if (name.contains("Card")) {
			descriptionString = "Greeting Card";
		}
		return descriptionString;
	}

	public String getPosterSize() {
		String size = "Size Not Found";
		String[] parts = name.split("x");
		if (parts.length < 2) {
			return size; // has to have "x" in it order to have a size
		}
		String[] parts1Split = parts[0].split(" ");
		String size1 = parts1Split[parts1Split.length - 1]; // thing before the x
		String[] parts2Split = parts[1].split(" ");
		String size2 = parts2Split[0]; // thing after the x
		size = size1 + "x" + size2; // put it back together
		return size;
	}

}
