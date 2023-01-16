package com.bmxApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.bmxApp.enums.Part;
import com.bmxApp.model.Product;
import com.bmxApp.repository.ProductRepository;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.MainControllerService;

@Configuration
@EnableAsync
@EnableScheduling
public class ProductDatabaseUpdater {

	@Autowired
	ProductRepository productDatabaseHandler;
	
	@Autowired
	ShopResearcherService shopResearcher;
	
	@Autowired
	MainControllerService mainControllerService;
	
	@Scheduled(fixedDelay = 60000)
	@Async
	public void updateProductDatabase() {
		//shopResearcher.getProductsArray().clear();
		//shopResearcher.searchNewProducts();
		//Iterable<Product> actualProducts = productDatabaseHandler.findAll();
	/*	Part[] parts = Part.values();
		for(int counter = 0; counter < parts.length; counter++)
		mainControllerService.setResearcherAllShops(parts[counter].name(), true);*/
	}
	
}
