package com.bmxApp.handler;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;



@Repository
public interface BasketProductDatabaseHandler extends CrudRepository<BasketProduct, Integer> {

	List<BasketProduct> findByShopName(String shopName);
	@Query(value = "Select (bt.quantity * pt.price) from BasketProduct bt inner join Product pt on bt.product = pt.id where bt.id=?1")
	public float getTotalPrice(int id);
}
