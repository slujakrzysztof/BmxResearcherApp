package com.bmxApp.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bmxApp.properties.PropertyReader;
import com.bmxApp.researcher.ShopResearcher;

@Service
public class MainControllerService {

	@Autowired
	DatabaseService databaseService;
	
	ArrayList<ShopResearcher> usedResearcherArray = new ArrayList<ShopResearcher>();

	private boolean firstInitialized = true;
	private boolean partSearched = false;

	private String category, partName, shopName;
	
	@Value("polish")
	private String language;

	@Autowired(required = false)
	ShopResearcher shopResearcher;
	
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
	/*
	 * public void startSearching(String partName, boolean value) { this.partName =
	 * partName; if (frame.getFeaturePanel().getAvebmxBox().isSelected() ||
	 * frame.getFeaturePanel().getBmxlifeBox().isSelected() ||
	 * frame.getFeaturePanel().getManyfestbmxBox().isSelected() ||
	 * frame.getFeaturePanel().getAlldayBox().isSelected()) setResearcher(partName,
	 * shopName, shopNumber, value); else if
	 * (frame.getFeaturePanel().getAllShopsBox().isSelected())
	 * setResearcher(nameOfPart, nameOfShop, shopNumber, value); }
	 */
}
