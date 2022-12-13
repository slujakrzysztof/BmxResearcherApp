package com.bmxApp.service;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Soundbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bmxApp.enums.Part;
import com.bmxApp.enums.Shop;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.researcher.ShopResearcher;

@Service
public class MainControllerService {

	@Autowired
	ProductDatabaseService databaseService;
	
	@Autowired
	BasketProductDatabaseService basketProductDatabaseService;

	ArrayList<ShopResearcher> usedResearcherArray = new ArrayList<ShopResearcher>();

	private boolean firstInitialized = true;
	private boolean partSearched = false;

	private String category, partName, shopName;

	private String language = "polish";

	@Autowired(required = false)
	ShopResearcher shopResearcher;

	public ProductDatabaseService getDatabaseService() {
		return this.databaseService;
	}

	public ShopResearcher getShopResearcher() {
		return this.shopResearcher;
	}
	
	public List<BasketProduct> getBasketProducts(){
		return basketProductDatabaseService.getAllBasketProducts();
	}
	

	public void setResearcher(String category, String shopName, boolean partSelection) {

		try {
			this.setPropertyReader(shopName);

			String html = "";
			if (partSelection)
				html = PropertyReader.getInstance().getProperty("url");
			else
				html = PropertyReader.getInstance().getProperty("safetyURL");

			System.out.println("htmmml: " + html);

			System.out.println("CZESC: " + category);

			shopResearcher.setHTML(html);
			shopResearcher.setShopName(shopName);
			shopResearcher.setConnection();
			shopResearcher.setCategory(category);
			shopResearcher.searchPage();
			shopResearcher.setPagesArray();
			shopResearcher.setHTML(shopResearcher.getPagesArray().get(0));
			shopResearcher.setConnection();

			shopResearcher.setInitialized(true);
			if (!this.partPreviousSearched(shopName, category)) {
				System.out.println("ZACZYNAM SZUKAĆ");
				shopResearcher.setProductUpdated(false);
				shopResearcher.searchNewProducts();

			} else {
				shopResearcher.setProductUpdated(true);
				// ---- NA RZECZ TESTÓW ----//
				// shopResearcher.searchPreviousProducts(shopName, category);
			}

			shopResearcher.setSpecificInformations(category);
			this.setPartSearched(true);

		} catch (NullPointerException ex) {
			this.removeResearcher(this.getResearcher(shopName));
			System.out.println("SIEMANO JESTEM TU");
			ex.printStackTrace();
		}
	}
	
	//Iterate all available shops to get products
	public void setResearcherAllShops(String category, boolean partSelection) {
		for(Shop shop : Shop.getShops())
			this.setResearcher(category, shop.name().toLowerCase(), partSelection);
	}

	public String getLanguage() {
		return this.language;
	}

	public boolean getPartSearched() {
		return this.partSearched;
	}

	public void setPropertyReader(String filename) {
		// this.resource = ResourceBundle.getBundle("properties/" + getLanguage());
		PropertyReader.getInstance().setPropertyFilename("com/bmxApp/properties/" + filename + ".properties");
		System.out.println("Jestem tu: " + PropertyReader.getInstance().getFilename());
		PropertyReader.getInstance().setConnection();
	}

	public boolean wasShopUsed(String shopName) {
		return databaseService.wasShopUsed(shopName);
	}

	public boolean partPreviousSearched(String shopName, String category) {
		return databaseService.wasPartSearchedPrevious(shopName, category);
	}

	public ShopResearcher getResearcher(String shopName) {
		for (int index = 0; index < usedResearcherArray.size(); index++) {
			if (usedResearcherArray.get(index).getShopName().equals(shopName))
				return this.usedResearcherArray.get(index);
		}
		return null;
	}

	public void removeResearcher(ShopResearcher researcher) {
		usedResearcherArray.remove(researcher);
	}

	public ArrayList<ShopResearcher> getResearcherArray() {
		return this.usedResearcherArray;
	}

	public void setFirstInitialized(boolean firstInitialized) {
		this.firstInitialized = firstInitialized;
	}

	public void setPartSearched(boolean partSearched) {
		this.partSearched = partSearched;
	}
}
