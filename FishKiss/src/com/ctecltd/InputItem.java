package com.ctecltd;

import java.util.HashMap;
import java.util.Map;

public class InputItem {

	public String date;
	public String name;
	public String quantity;
	public String total;
	public String sku;
	public String variations;
	public String discountAmount;
	public String status;
	public String keyWord;

	Map<String, String> statesMap = new HashMap<>();
	{
		statesMap.put("AL", "Alabama");
		statesMap.put("AK", "Alaska");
		statesMap.put("AR", "Arkansas");
		statesMap.put("AZ", "Arizona");
		statesMap.put("CA", "California");
		statesMap.put("CO", "Colorado");
		statesMap.put("CT", "Connecticut");
		statesMap.put("DE", "Delaware");
		statesMap.put("FL", "Florida");
		statesMap.put("GA", "Georgia");
		statesMap.put("HI", "Hawaii");
		statesMap.put("IN", "Indiana");
		statesMap.put("IA", "Iowa");
		statesMap.put("ID", "Idaho");
		statesMap.put("IL", "Illinois");
		statesMap.put("KY", "Kentucky");
		statesMap.put("KS", "Kansas");
		statesMap.put("LA", "Louisiana");
		statesMap.put("ME", "Maine");
		statesMap.put("MD", "Maryland");
		statesMap.put("MA", "Massachusetts");
		statesMap.put("MI", "Michigan");
		statesMap.put("MN", "Minnesota");
		statesMap.put("MS", "Mississippi");
		statesMap.put("MO", "Missouri");
		statesMap.put("MT", "Montana");
		statesMap.put("NE", "Nebraska");
		statesMap.put("NV", "Nevada");
		statesMap.put("NH", "New Hampshire");
		statesMap.put("NJ", "New Jersey");
		statesMap.put("NM", "New Mexico");
		statesMap.put("NY", "New York");
		statesMap.put("NC", "North Carolina");
		statesMap.put("ND", "North Dakota");
		statesMap.put("OR", "Oregon");
		statesMap.put("OH", "Ohio");
		statesMap.put("OK", "Oklahoma");
		statesMap.put("PA", "Pennsylvania");
		statesMap.put("RI", "Rhode Island");
		statesMap.put("SD", "South Dakota");
		statesMap.put("SC", "South Carolina");
		statesMap.put("TX", "Texas");
		statesMap.put("TN", "Tennesee");
		statesMap.put("UT", "Utah");
		statesMap.put("VA", "Virginia");
		statesMap.put("VT", "Vermont");
		statesMap.put("WV", "West Virginia");
		statesMap.put("WA", "Washington");
		statesMap.put("WI", "Wisconsin");
		statesMap.put("WY", "Wyoming");
		statesMap.put("DC", "Washington D.C.");
		statesMap.put("NYC", "New York City");
		statesMap.put("BI", "Block Island");
		statesMap.put("WH", "Watch Hill");
	}

	public String getState() {
		String state = sku.replace("CUS-", "");
		state = state.substring(0, 2);

		state = statesMap.get(state);

		return state;
	}

	public String getDescription() throws BadItemNameException {
		String description = keyWord;
		try {

			if (isArtItem()) {
				description = getArtDescription();
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

	public String getArtDescription() {
		String description = keyWord;
		if (description.equals("Map") || description.equals("Art")) {
			variations = variations.replace("&quot;", "");
			String size = getPosterSize();
			description = size + " Poster";
			String[] nameSplit = name.split(",");
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
		return description;
	}

	public boolean isArtItem() {
		if (keyWord == null)
			return false;
		return (keyWord.equals("Map") || keyWord.equals("Art") || keyWord.equals("Canvas"));
	}

	public String getPosterSize() {
		String size = "Size Not Found";
		if (variations.length() > 9) {
			size = variations.substring(5, 10);
		}
		return size;
	}

	public String getCategory() throws BadItemNameException {
//		String[] nameSplit = name.split(",")[0].split(" ");
//		String raw_description = nameSplit[nameSplit.length - 1];
		return Category.getCategory(getDescription());
	}

	public String getCompany() throws BadItemNameException {
		return Company.getCompany(getDescription());
	}

	public String getDiscountAmount() {
		return discountAmount;
	}

	public String getSku() {
		return sku;
	}

	public String getDate() {
		return date;
	}

}
