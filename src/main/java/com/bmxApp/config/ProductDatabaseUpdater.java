package com.bmxApp.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.Part;
import com.bmxApp.enums.Shop;
import com.bmxApp.manager.PropertyManager;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.repository.ProductRepository;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.main.MainControllerService;
import com.bmxApp.service.product.ProductRepositoryService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class ProductDatabaseUpdater {

	final ProductRepositoryService productRepositoryService;
	final ShopResearcherService shopResearcherService;
	
	
	
	//Update products' database once per day
	@Scheduled(fixedDelay = 86_400_000)
	@Async
	public void updateProductDatabase() {
		
		List<Shop> shopList = Arrays.asList(Shop.values());
		
		List<Shop> shops = shopList.stream().limit(shopList.size() - 1).collect(Collectors.toList());
				
		List<Part> partList = Arrays.asList(Part.values());
		
		shops.forEach(shop -> {
			
			String shopName = shop.name().toLowerCase();
			PropertyReader.getInstance().connectPropertyReader(shopName);
			String html = PropertyManager.getInstance().URL();
			
			partList.forEach(part ->
					{
			
				shopResearcherService.setConnection(html);
				String category = part.name().toLowerCase();
				String partUrl = shopResearcherService.findPartUrl(category);
				
				shopResearcherService.setConnection(partUrl);
				shopResearcherService.getProductsFromPage(shopName);
				
				ArrayList<ProductDTO> newProducts = (ArrayList<ProductDTO>) 
									 shopResearcherService.getFormattedDataProducts(shopName, category).stream()
									 .filter(dtoProduct -> !productRepositoryService.isProductInDatabase(dtoProduct))
									 .collect(Collectors.toList());
				
				newProducts.forEach(dtoProduct -> productRepositoryService.insertUpdateProduct(dtoProduct));
			
			});
		});
		
	}
	
}
