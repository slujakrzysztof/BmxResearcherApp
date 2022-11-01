package com.bmxApp.handler;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bmxApp.model.Product;



@Repository
public interface ProductDatabaseHandler extends CrudRepository<Product, Integer> {

	//List<ShopProduct> findByCategory(String category);
	List<Product> findByShopNameAndCategory(String shopName, String category); 
	List<Product> findByShopName(String shopName);
	Product findByProductNameAndShopName(String productName, String shopName);
}
