package com.bmxApp.manager;

import com.bmxApp.properties.PropertyReader;

public final class PropertyManager {

	//private final PropertyReader propertyReader = PropertyReader.getInstance();
	
	public static PropertyManager propertyManager = new PropertyManager();
	
	/*public String GRIPS = propertyReader.getProperty("grips");
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
	public String PEGS = propertyReader.getProperty("pegs");*/
	
	
	public String SHOP_NAME() {
		return PropertyReader.getInstance().getProperty("shopName");
	}
	
	public String URL() {
		return PropertyReader.getInstance().getProperty("url");
	}
	
	public String DESCRIPTION() {
		return PropertyReader.getInstance().getProperty("description");
	}
	
	public String SAFETY_URL() {
		return PropertyReader.getInstance().getProperty("safetyURL");
	}
	
	public String PRODUCT_NAME() {
		return PropertyReader.getInstance().getProperty("productName");
	}
	
	public String PRODUCT_PRICE() {
		return PropertyReader.getInstance().getProperty("productPrice");
	}
	
	public String PRODUCT_PRICE_DISCOUNT() {
		return PropertyReader.getInstance().getProperty("productDiscountPrice");
	}
	
	public String PRODUCT_URL() {
		return PropertyReader.getInstance().getProperty("productURL");
	}
	
	public String URL_ATTRIBUTE() {
		return PropertyReader.getInstance().getProperty("urlAtrribute");
	}
	
	public String IMAGE_URL() {
		return PropertyReader.getInstance().getProperty("imageURL");
	}
	
	public String IMAGE_ATTRIBUTE() {
		return PropertyReader.getInstance().getProperty("imageAttribute");
	}
	
	public String URL_SEARCH_PAGE() {
		return PropertyReader.getInstance().getProperty("urlSearchPage");
	}
	
	public String PAGE_NUMBER() {
		return PropertyReader.getInstance().getProperty("pagesNumber");
	}
	
	public String ALL_PRODUCTS_DISPLAY() {
		return PropertyReader.getInstance().getProperty("allProductsDisplay");
	}
	
	public String DIV() {
		return PropertyReader.getInstance().getProperty("div");
	}
	
	public String ABS_URL_ATTRIBUTE() {
		return "href";
	}
	
	public static PropertyManager getInstance() {
		return propertyManager;
	}
	
}

