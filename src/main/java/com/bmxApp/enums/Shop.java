package com.bmxApp.enums;

import java.util.EnumSet;

public enum Shop {

	//---!!!--- KEEP ALLSHOPS ALWAYS AS THE LAST ELEMENT ---!!!---
	
	BMXLIFE, 
	AVEBMX, 
	MANYFESTBMX, 
	ALLSHOPS;
	
	// While Allday hasn't been configured this method doesn't return it - ADD LATER
	public static EnumSet<Shop> getShops() {
		return EnumSet.of(Shop.BMXLIFE, Shop.AVEBMX, Shop.MANYFESTBMX);
	}
	
}
