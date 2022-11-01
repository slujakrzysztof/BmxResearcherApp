package com.bmxApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.bmxApp.handler.ProductDatabaseHandler;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.ShopProduct;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.researcher.ShopResearcher;

import jakarta.transaction.Transactional;

@Service
public class ResearcherControllerService {

	@Autowired
	ProductDatabaseHandler productDatabaseHandler;
	@Autowired
	DatabaseService databaseService;

	@Autowired
	MainControllerService mainControllerService;

	@Autowired(required = false)
	ShopResearcher shopResearcher;

	public void setResearcher(String category, String shopName, int shopNumber, boolean partSelection) {

		try {
			mainControllerService.setPropertyReader(shopName);

			String html = "";
			if (partSelection)
				html = PropertyReader.getInstance().getProperty("url");
			else
				html = PropertyReader.getInstance().getProperty("safetyURL");

			System.out.println("htmmml: " + html);

			// {
			/*
			 * if (!mainControllerService.wasShopUsed(shopName)) { shopResearcher = new
			 * ShopResearcher(html, shopName);
			 * mainControllerService.getResearcherArray().add(shopResearcher); } else {
			 * shopResearcher = mainControllerService.getResearcher(shopName);
			 * shopResearcher.clearProductsArray(); shopResearcher.setHTML(html); }
			 */
			// shopResearcher.setFrame(this.frame);

			shopResearcher.setHTML(html);
			shopResearcher.setShopName(shopName);
			shopResearcher.setConnection();
			shopResearcher.searchPage(category);

			shopResearcher.setInitialized(true);
			if (!mainControllerService.partPreviousSearched(shopName, category)) {
				System.out.println("ZACZYNAM SZUKAĆ");
				shopResearcher.searchNewProducts();

			}
			else
				shopResearcher.searchPreviousProducts(shopName, category);

			shopResearcher.setSpecificInformations(category);
			// shopResearcher.initializePartPanel(true);
			// } else {
			// mainControllerService.getResearcher(shopName).clearProductsArray();
			// mainControllerService.getResearcher(shopName).setSpecificInformations(category);
			// getResearcher(shopName).initializePartPanel(false);
			// }
			// mainControllerService.setFirstInitialized(false);
			mainControllerService.setPartSearched(true);

		} catch (NullPointerException ex) {

			// frame.setActivePanel(frame.getMainLabel());
			mainControllerService.removeResearcher(mainControllerService.getResearcher(shopName));
			System.out.println("SIEMANO JESTEM TU");
			ex.printStackTrace();
		}
	}

	@Transactional
	public void insertProduct() {
		databaseService.insertOrUpdateProduct(new ShopProduct("haha", "hahaha", "bbbb", "cccc", "nicnic", 1200.0d));
		databaseService.insertOrUpdateProduct(new BasketProduct("nic", "lol", "nic2", 500.0d));
		// databaseService.deleteAllProducts();
		System.out.println("a: " + databaseService.wasPartSearchedPrevious("hahahas", "bbbb"));
	}

}
