package com.bmxApp.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.bmxApp.enums.Part;
import com.bmxApp.enums.Shop;
import com.bmxApp.manager.PropertyManager;
import com.bmxApp.properties.PropertyReader;
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
	ShopResearcherService shopResearcherService;
	
	@Autowired
	MainControllerService mainControllerService;
	
	@Scheduled(fixedDelay = 60000)
	@Async
	public void updateProductDatabase() {
		
		EnumSet<Shop> shops = Shop.getShops();
		List<Part> parts = Arrays.asList(Part.values());
		
		shops.forEach(shop -> {
			
			String shopName = shop.name().toLowerCase();
			PropertyReader.getInstance().connectPropertyReader(shopName);
			String html = PropertyManager.getInstance().URL();
			
			parts.forEach(part -> {
			
				shopResearcherService.setConnection(html);
				String category = part.name().toLowerCase();
				String partUrl = shopResearcherService.findPartUrl(category);
				
				shopResearcherService.setConnection(partUrl);
				
				shopResearcherService.searchNewProducts(shopName, category, partUrl);
			});
		});
		
	}
	
}
