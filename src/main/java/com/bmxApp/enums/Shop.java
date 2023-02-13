package com.bmxApp.enums;

import java.util.ArrayList;
import java.util.List;

public enum Shop {

	//---!!!--- KEEP ALLSHOPS ALWAYS AS THE LAST ELEMENT ---!!!---
	
	BMXLIFE, 
	AVEBMX, 
	MANYFESTBMX, 
	ALLSHOPS;
	
	// While Allday hasn't been configured this method doesn't return it - ADD LATER
	public static List<Shop> getShops() {
		
		List<Shop> shopList = new ArrayList<>();
		shopList.add(BMXLIFE);
		shopList.add(AVEBMX);
		shopList.add(MANYFESTBMX);
		return shopList;
	}
	
}
