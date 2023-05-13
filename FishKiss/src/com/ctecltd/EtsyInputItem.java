package com.ctecltd;

import org.apache.commons.csv.CSVRecord;

public class EtsyInputItem extends InputItem {

	public EtsyInputItem(CSVRecord record) {
		this.date = record.get("Sale Date");
		this.name = record.get("Item Name");
		this.quantity = record.get("Quantity");
		this.total = record.get("Item Total");
		this.variations = record.get("Variations");
		this.sku = record.get("SKU");
		this.discountAmount = record.get("Discount Amount");
		String[] nameSplit = name.split(",")[0].split(" ");
		this.keyWord = nameSplit[nameSplit.length - 1];
	}

	public String getState() {
		String[] nameSplit = name.split(" ");
		String state = nameSplit[0];
		int index = 0;
		if (state.equals("Custom")) {
			state = nameSplit[1];
			index = 1;
		}
		if (state.equals("New") || state.equals("Rhode") || state.equals("North") || state.equals("South")
				|| state.equals("West")) {
			state += " " + nameSplit[index + 1];
		}
		if (state.equals("Washington")) {
			if (nameSplit[index + 1].contains("D.C") || nameSplit[index + 1].contains("DC")) {
				state = "Washington D.C.";
			}
		}
		return state;
	}

}
