package com.bmxApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bmxApp.model.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


	List<Product> findByShopNameAndCategory(String shopName, String category);

	List<Product> findByShopName(String shopName);

	Product findByProductNameAndShopName(String productName, String shopName);

	List<Product> findByCategoryAndShopName(String category, String shopName);
	
	Product findById(int id);
	
	List<Product> findByCategory(String category);
	
	List<Product> findByProductName(String productName);
	
	@Query(value = "SELECT p from Product p WHERE p.productName LIKE %?1%")
	List<Product> findRequestedItems(String value);
	
	/*@Query(value = "SELECT DISTINCT category from product_table")
	List<Product> getAllExistingCategories();
	
	@Query(value = "SELECT DISTINCT shopName from product_table")
	List<Product> getAllExistingShops();*/
}
