package com.bmxApp.handler;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bmxApp.model.Product;

@Repository
public interface ProductDatabaseHandler extends CrudRepository<Product, Integer> {


	List<Product> findByShopNameAndCategory(String shopName, String category);

	List<Product> findByShopName(String shopName);

	Product findByProductNameAndShopName(String productName, String shopName);

	List<Product> findByCategoryAndShopName(String category, String shopName);
	
	Product findById(int id);
	
	List<Product> findByCategory(String category);
	
	/*@Query(value = "SELECT DISTINCT category from product_table")
	List<Product> getAllExistingCategories();
	
	@Query(value = "SELECT DISTINCT shopName from product_table")
	List<Product> getAllExistingShops();*/
}
