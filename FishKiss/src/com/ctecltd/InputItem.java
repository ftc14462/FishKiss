package com.ctecltd;

public class InputItem {

	public String date;
	public String name;
	public String quantity;
	public String total;
	public String sku;
	public String variations;

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

	public String getDescription() throws BadItemNameException {
		String description = "";
		try {
			String[] nameSplit = name.split(",")[0].split(" ");
			description = nameSplit[nameSplit.length - 1];

			if (description.equals("Map") || description.equals("Art")) {
				variations = variations.replace("&quot;", "");
				String size = "Size Not Found";
				if (variations.length() > 9) {
					size = variations.substring(5, 10);
				}
				description = size + " Poster";
				nameSplit = name.split(",");
				String last = nameSplit[nameSplit.length - 1];
				if (last.contains("Printed on watercolor paper")) {
					description = size;
				}
				if (size.equals("Greet")) {
					description = "Greeting Card";
				}
			}

			if (description.equals("Canvas")) {
				variations = variations.replace("&quot;", "");
				variations = variations.replace(" ", "");
				String size = "Size Not Found";
				if (variations.length() > 9) {
					size = variations.substring(5, 10);
				}
				description = size + " Canvas";
			}

			if (description.equals("Pillow")) {
				if (variations.contains("Cover Only")) {
					description = "Pillow Cover";
				}
			}
		} catch (Exception e) {
			throw new BadItemNameException(e, name);
		}

		return description;
	}

	public String getCategory() throws BadItemNameException {
//		String[] nameSplit = name.split(",")[0].split(" ");
//		String raw_description = nameSplit[nameSplit.length - 1];
		return Category.getCategory(getDescription());
	}

	public String getCompany() throws BadItemNameException {
		return Company.getCompany(getDescription());
	}

}
