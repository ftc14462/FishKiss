package com.ctecltd;

import java.util.HashMap;

public class Category {

	private static HashMap<String, String> hashmap;

	public static String getCategory(String description) {
		String[] split = description.split(" ");
		String short_description = split[split.length - 1]; // last word of description
		if (description.contains("x")) {
			short_description = "Art";
		}
		if (hashmap == null) {
			hashmap = new HashMap<>();
			hashmap.put("Glass", "Drinkware");
			hashmap.put("Mug", "Drinkware");
			hashmap.put("Pillow", "Pillow");
			hashmap.put("Cover", "Pillow");
			hashmap.put("Art", "Art/Stationary");
			hashmap.put("Towel", "Towel");
			hashmap.put("Hat", "Baby");
			hashmap.put("Headband", "Baby");
			hashmap.put("One-Piece", "Baby");
			hashmap.put("Card", "Art/Stationary");
			hashmap.put("Tumbler", "Drinkware");
			hashmap.put("Blanket", "Baby");
		}
//		String category="Not found";
		String category = hashmap.get(short_description);
		if (category == null) {
			category = "Not found";
		}
		return category;
	}

}
