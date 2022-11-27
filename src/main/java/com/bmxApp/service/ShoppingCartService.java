package com.bmxApp.service;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.handler.BasketProductDatabaseHandler;
import com.bmxApp.model.BasketProduct;

@Service
public class ShoppingCartService {

	@Autowired
	private BasketProductDatabaseHandler basketProductDatabaseHandler;

	public List<BasketProduct> getAllProducts() {
		return IterableUtils.toList(basketProductDatabaseHandler.findAll());
	}

	public float getTotalPriceForProduct(int id) {
		return basketProductDatabaseHandler.getTotalPriceForProduct(id);
	}

	public float getTotalPrice() {
		if (IterableUtils.toList(basketProductDatabaseHandler.findAll()).isEmpty())
			return 0f;
		return basketProductDatabaseHandler.getTotalPrice();
	}

}
