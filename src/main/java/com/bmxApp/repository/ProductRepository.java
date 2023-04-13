package com.bmxApp.repository;

import java.math.BigDecimal;
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
	
	@Query(value = "SELECT p from Product p WHERE (p.productName LIKE %?1%) OR (p.shopName LIKE %?1%)")
	List<Product> findRequestedItems(String value);
	
	@Query(value = "SELECT p.price FROM Product p WHERE (p.productName LIKE %?1%) OR (p.shopName LIKE %?1%)")
	BigDecimal getPrice(String productName, String shopName);
	
}
