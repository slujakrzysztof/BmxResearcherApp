package com.bmxApp.enums;

import java.util.EnumSet;

public enum Shop {

	BMXLIFE, 
	AVEBMX, 
	MANYFESTBMX, 
	ALLDAY, 
	ALLSHOPS;

	// While Allday hasn't been configured this method doesn't return it - ADD LATER
	public static EnumSet<Shop> getShops() {
		return EnumSet.of(Shop.BMXLIFE, Shop.AVEBMX, Shop.MANYFESTBMX);
	}
	
}
