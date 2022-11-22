package com.bmxApp.service;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.bmxApp.handler.BasketProductDatabaseHandler;
import com.bmxApp.handler.ProductDatabaseHandler;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;

import jakarta.transaction.Transactional;

@Service
public class DatabaseService {

	@Autowired
	ProductDatabaseHandler productDatabaseHandler;
	@Autowired
	BasketProductDatabaseHandler basketProductDatabaseHandler;

	@Transactional
	public List<Product> getAllProducts() {
		return IterableUtils.toList(productDatabaseHandler.findAll());
	}

	@Transactional
	public void insertOrUpdateProduct(Product product) {
		productDatabaseHandler.save(product);
	}

	@Transactional
	public List<Product> getProductsByCategoryAndShopName(String category, String shopName) {
		return productDatabaseHandler.findByCategoryAndShopName(category, shopName);
	}

	@Transactional
	public void insertAllProducts(Iterable<Product> products) {
		productDatabaseHandler.saveAll(products);
	}

	@Transactional
	public void deleteProduct(Product product) {
		productDatabaseHandler.delete(product);
	}

	@Transactional
	public void deleteAllProducts() {
		productDatabaseHandler.deleteAll();
	}

	@Transactional
	public boolean wasPartSearchedPrevious(String shopName, String category) {
		if (productDatabaseHandler.findByShopNameAndCategory(shopName, category).isEmpty())
			return false;
		return true;
	}

	public boolean wasShopUsed(String shopName) {
		if (productDatabaseHandler.findByShopName(shopName).isEmpty())
			return false;
		return true;
	}

	@Transactional
	public Product getProductByName(String productName, String shopName) {
		return productDatabaseHandler.findByProductNameAndShopName(productName, shopName);
	}

	public List<BasketProduct> getBasketProducts() {
		return IterableUtils.toList(basketProductDatabaseHandler.findAll());
	}
}
