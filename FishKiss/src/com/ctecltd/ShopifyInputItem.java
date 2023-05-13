package com.ctecltd;

import org.apache.commons.csv.CSVRecord;

public class ShopifyInputItem extends InputItem {

	public ShopifyInputItem(CSVRecord record) {
		this.date = "N/A";
		this.name = record.get("product_title");
		this.quantity = record.get("net_quantity");
		this.total = record.get("total_sales");
		this.variations = record.get("variant_title");
		this.sku = record.get("variant_sku");
		this.discountAmount = record.get("discounts");

		String nameShort = name.replace(" - JERSEY", ""); // for shopify input
		nameShort = nameShort.replace(" - PIMA", ""); // for shopify input

		String[] nameSplit = nameShort.split(",")[0].split(" ");
		this.keyWord = nameSplit[nameSplit.length - 1];
	}

	public boolean isArtItem() {
		if (name == null)
			return false;
		return (name.contains("Poster") || name.contains("Print") || name.contains("Canvas") || name.contains("Card"));
	}

	public String getPosterSize() {
		String size = "Size Not Found";
		if (variations.contains("x")) {
			size = variations;
		}
		return size;
	}

	public String getArtDescription() {
		String descriptionString = "unkown art type";
		if (name == null) {
			return descriptionString;
		}
		String sizeString = getPosterSize();
		if (name.contains("Poster")) {
			descriptionString = sizeString + " Poster";
		}
		if (name.contains("Print")) {
			descriptionString = sizeString;
		}
		if (name.contains("Canvas")) {
			descriptionString = sizeString + " Canvas";
		}
		if (name.contains("Card")) {
			descriptionString = "Greeting Card";
		}
		return descriptionString;
	}

}
