package com.bmxApp.handler;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;



@Repository
public interface BasketProductDatabaseHandler extends CrudRepository<BasketProduct, Integer> {

	List<BasketProduct> findByProduct(Product product);
}
