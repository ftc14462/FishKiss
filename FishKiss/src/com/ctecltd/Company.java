package com.ctecltd;

import java.util.HashMap;

public class Company {

	private static HashMap<String, String> hashmap;

	public static String getCompany(String description) {
		String[] split = description.split(" ");
		String short_description = split[split.length - 1]; // last word of description
		if (description.contains("x")) {
			short_description = "Art";
		}
		if (hashmap == null) {
			hashmap = new HashMap<>();
			hashmap.put("Glass", "Dubow");
			hashmap.put("Mug", "Dubow");
			hashmap.put("Pillow", "Jondo");
			hashmap.put("Cover", "Jondo");
			hashmap.put("Art", "Jondo");
			hashmap.put("Towel", "Fish Kiss");
			hashmap.put("Hat", "Fish Kiss");
			hashmap.put("Bib", "Fish Kiss");
			hashmap.put("Headband", "Fish Kiss");
			hashmap.put("One-Piece", "Fish Kiss");
			hashmap.put("Card", "Fish Kiss");
			hashmap.put("Tumbler", "Dubow");
			hashmap.put("Blanket", "Fish Kiss");
			hashmap.put("Platter", "Decoplate");
			hashmap.put("Bowl", "Decoplate");
			hashmap.put("Plate", "Decoplate");
		}
//		String category="Not found";
		String company = hashmap.get(short_description);
		if (company == null) {
			company = "No Company Match For: " + short_description;
		}
		return company;
	}

}
