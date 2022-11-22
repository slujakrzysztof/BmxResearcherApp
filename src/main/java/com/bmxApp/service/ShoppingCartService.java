package com.bmxApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.model.BasketProduct;

@Service
public class ShoppingCartService {

	@Autowired
	private DatabaseService databaseService;

	public List<BasketProduct> listAllItems(){
		return databaseService.getBasketProducts();
	}
}
