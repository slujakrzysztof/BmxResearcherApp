package com.bmxApp.handler;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bmxApp.model.BasketProduct;



@Repository
public interface BasketProductDatabaseHandler extends CrudRepository<BasketProduct, Integer> {

	List<BasketProduct> findByProductName(String productName);
}
