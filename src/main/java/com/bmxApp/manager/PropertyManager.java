package com.bmxApp.manager;

import com.bmxApp.properties.PropertyReader;

public final class PropertyManager {

	private final PropertyReader propertyReader = PropertyReader.getInstance();
	
	public static PropertyManager propertyManager = new PropertyManager();
	
	public  String SHOP_NAME = propertyReader.getProperty("shopName");
	public  String URL = propertyReader.getProperty("url");
	public  String DESCRIPTION = propertyReader.getProperty("description");
	public  String SAFETY_URL = propertyReader.getProperty("safetyURL");
	public  String PRODUCT_NAME = propertyReader.getProperty("productName");
	public  String PRODUCT_PRICE = propertyReader.getProperty("productPrice");
	public  String PRODUCT_PRICE_DISCOUNT = propertyReader.getProperty("productDiscountPrice");
	public  String PRODUCT_URL = propertyReader.getProperty("productURL");
	public  String URL_ATTRIBUTE = propertyReader.getProperty("urlAtrribute");
	public  String IMAGE_URL = propertyReader.getProperty("imageURL");
	public  String IMAGE_ATTRIBUTE = propertyReader.getProperty("imageAttribute");
	public  String URL_SEARCH_PAGE = propertyReader.getProperty("urlSearchPage");
	public  String PAGE_NUMBER = propertyReader.getProperty("pagesNumber");
	public  String ALL_PRODUCTS_DISPLAY = propertyReader.getProperty("allProductsDisplay");
	public  String DIV = propertyReader.getProperty("div");
	public 	String ABS_URL_ATTRIBUTE = "href";
	
	public String GRIPS = propertyReader.getProperty("grips");
	public String BARS = propertyReader.getProperty("bars");
	public String BARENDS = propertyReader.getProperty("barends");
	public String STEMS = propertyReader.getProperty("stems");
	public String HEADS = propertyReader.getProperty("heads");
	public String FRAMES = propertyReader.getProperty("frames");
	public String FORKS = propertyReader.getProperty("forks");
	public String RIMS = propertyReader.getProperty("rims");
	public String TIRES = propertyReader.getProperty("tires");
	public String SPOKES = propertyReader.getProperty("spokes");
	public String HUBS = propertyReader.getProperty("hubs");
	public String POSTS = propertyReader.getProperty("posts");
	public String GEARS = propertyReader.getProperty("gears");
	public String CRANKS = propertyReader.getProperty("cranks");
	public String PEDALS = propertyReader.getProperty("pedals");
	public String CHAINS = propertyReader.getProperty("chains");
	public String SEATS = propertyReader.getProperty("seats");
	public String SUPPORTS = propertyReader.getProperty("supports");
	public String PEGS = propertyReader.getProperty("pegs");
	

	public static PropertyManager getInstance() {
		return propertyManager;
	}
	
}

