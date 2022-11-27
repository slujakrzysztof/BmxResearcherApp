package com.bmxApp.service;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.handler.BasketProductDatabaseHandler;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;

@Service
public class BasketProductDatabaseService {

	@Autowired
	BasketProductDatabaseHandler basketProductDatabaseHandler;

	public float getTotalPrice() {
		return basketProductDatabaseHandler.getTotalPrice();
	}

	public float getTotalPriceForProduct(int id) {
		return basketProductDatabaseHandler.getTotalPriceForProduct(id);
	}

	public BasketProduct findByProduct(Product product) {
		return basketProductDatabaseHandler.findByProduct(product);
	}

	public List<BasketProduct> getAllBasketProducts() {
		return IterableUtils.toList(basketProductDatabaseHandler.findAll());
	}

	public boolean productAdded(Product product) {
		if(basketProductDatabaseHandler.findByProduct(product) != null) return true;
		return false;
	}
	
	public BasketProduct getProductByProductId(int productId) {
		return basketProductDatabaseHandler.findByProductId(productId);
	}
	
	public void insertOrUpdateBasketProduct(BasketProduct basketProduct) {
		basketProductDatabaseHandler.save(basketProduct);
	}
}