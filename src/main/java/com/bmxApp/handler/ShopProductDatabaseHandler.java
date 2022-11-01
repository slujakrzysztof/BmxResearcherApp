package com.bmxApp.handler;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bmxApp.model.ShopProduct;



public interface ShopProductDatabaseHandler extends CrudRepository<ShopProduct, Integer> {

	List<ShopProduct> findByCategory(String category);
}
