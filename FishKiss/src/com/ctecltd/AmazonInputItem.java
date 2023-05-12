package com.ctecltd;

import org.apache.commons.csv.CSVRecord;

public class AmazonInputItem extends InputItem {

	public AmazonInputItem(CSVRecord record) {
		this.date = record.get("purchase-date");
		this.name = record.get("product-name");
		this.quantity = record.get("quantity");
		this.total = record.get("item-price");
		this.variations = record.get("Variations");
		this.sku = record.get("sku");
		this.discountAmount = record.get("item-promotion-discount");
	}

}
