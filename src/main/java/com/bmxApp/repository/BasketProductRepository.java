package com.bmxApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;

import jakarta.transaction.Transactional;

@Repository
public interface BasketProductRepository extends CrudRepository<BasketProduct, Integer> {

	List<BasketProduct> findByShopName(String shopName);

	@Query(value = "Select (bt.quantity * pt.price) from BasketProduct bt inner join Product pt on bt.product = pt.id where bt.id=?1")
	public float getTotalPriceForProduct(int id);

	@Query(value = "Select SUM(bt.quantity * pt.price) from BasketProduct bt inner join Product pt on bt.product = pt.id")
	public float getTotalPrice();
	
	@Query(value = "Select SUM(bt.quantity * pt.price) from BasketProduct bt inner join Product pt on bt.product = pt.id where bt.shopName=?1")
	public float getTotalPriceByShopName(String shopName);
	
	@Query(value = "Select quantity from BasketProduct where id = ?1")
	public float getProductQuantity(int id);
	
	public BasketProduct findByProduct(Product product);
	
	BasketProduct findByProductId(int productId);
	
	BasketProduct findById(int id);
	

}
