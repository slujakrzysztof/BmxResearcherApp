package com.bmxApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.bmxApp.handler.ProductDatabaseHandler;
import com.bmxApp.researcher.ShopResearcher;

@Configuration
@EnableAsync
@EnableScheduling
public class ProductDatabaseUpdater {

	@Autowired
	ProductDatabaseHandler productDatabaseHandler;
	
	@Autowired
	ShopResearcher shopResearcher;
	
	@Scheduled
	@Async
	public void updateProductDatabase() {
		
	}
	
}
