package com.bmxApp.enums;

import lombok.Getter;

@Getter
public enum FilterItem {

	SHOP("shop"),
	PRODUCER("producer"),
	PRICE("price");
	
	private final String name;
	
	private FilterItem(String name) {
		this.name = name;
	}
	
	public static FilterItem fromString(String value) {
		
		for(FilterItem item : FilterItem.values())
			if(item.getName().equalsIgnoreCase(value)) return item;
			
		throw new IllegalArgumentException("Field not found");
	}
}
