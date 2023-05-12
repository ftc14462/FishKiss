package com.ctecltd;

import org.apache.commons.csv.CSVRecord;

public class ShopifyInputItem extends InputItem {

	public ShopifyInputItem(CSVRecord record) {
		this.date = "none";
		this.name = record.get("product_title");
		this.quantity = record.get("net_quantity");
		this.total = record.get("total_sales");
		this.variations = record.get("variant_title");
		this.sku = record.get("variant_sku");
		this.discountAmount = record.get("discounts");
	}

}
