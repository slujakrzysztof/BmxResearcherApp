package com.bmxApp.service;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;
import com.bmxApp.repository.BasketProductRepository;

@Service
public class BasketProductDatabaseService {

	@Autowired
	BasketProductRepository basketProductDatabaseHandler;

	public float getTotalPrice() {
		if(this.getAllBasketProducts().isEmpty()) return 0f;
		return basketProductDatabaseHandler.getTotalPrice();
	}
	
	public float getTotalPriceByShopName(String shopName) {
		return basketProductDatabaseHandler.getTotalPriceByShopName(shopName);
	}

	public float getTotalPriceForProduct(int id) {
		return basketProductDatabaseHandler.getTotalPriceForProduct(id);
	}
	
	public List<BasketProduct> getBasketProductsByShopName(String shopName){
		return basketProductDatabaseHandler.findByShopName(shopName);
	}

	public BasketProduct findByProduct(Product product) {
		return basketProductDatabaseHandler.findByProduct(product);
	}

	public List<BasketProduct> getAllBasketProducts() {
		return IterableUtils.toList(basketProductDatabaseHandler.findAll());
	}

	public boolean productAdded(Product product) {
		if (basketProductDatabaseHandler.findByProduct(product) != null)
			return true;
		return false;
	}

	public BasketProduct getProductByProductId(int productId) {
		return basketProductDatabaseHandler.findByProductId(productId);
	}

	public BasketProduct getProductById(int id) {
		return basketProductDatabaseHandler.findById(id);
	}

	public void insertOrUpdateBasketProduct(BasketProduct basketProduct) {
		basketProductDatabaseHandler.save(basketProduct);
	}

	public int getQuantity(int productId) {
		return (int) basketProductDatabaseHandler.getProductQuantity(productId);
	}
	
	public void deleteProducts() {
		basketProductDatabaseHandler.deleteAll();
	}
}