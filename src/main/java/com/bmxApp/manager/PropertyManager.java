package com.bmxApp.manager;

import com.bmxApp.properties.PropertyReader;

public final class PropertyManager {

	private final PropertyReader propertyReader = PropertyReader.getInstance();
	
	public final String SHOP_NAME = propertyReader.getProperty("shopName");
	public final String URL = propertyReader.getProperty("url");
	public final String DESCRIPTION = propertyReader.getProperty("description");
	public final String SAFETY_URL = propertyReader.getProperty("safetyURL");
	public final String PRODUCT_NAME = propertyReader.getProperty("productName");
	public final String PRODUCT_PRICE = propertyReader.getProperty("productPrice");
	public final String PRODUCT_PRICE_DISCOUNT = propertyReader.getProperty("productDiscountPrice");
	public final String PRODUCT_URL = propertyReader.getProperty("productURL");
	public final String URL_ATTRIBUTE = propertyReader.getProperty("urlAtrribute");
	public final String IMAGE_URL = propertyReader.getProperty("imageURL");
	public final String IMAGE_ATTRIBUTE = propertyReader.getProperty("imageAttribute");
	public final String URL_SEARCH_PAGE = propertyReader.getProperty("urlSearchPage");
	public final String PAGE_NUMBER = propertyReader.getProperty("pagesNumber");
	public final String ALL_PRODUCTS_DISPLAY = propertyReader.getProperty("allProductsDisplay");

}

