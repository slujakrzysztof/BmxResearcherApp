package com.bmxApp.mapper.product;

import java.math.BigDecimal;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.model.product.Product;

@Component
public class ProductMapper implements Function<ProductDTO, Product>{

	@Override
	public Product apply(ProductDTO productDTO) {

		Product product = new Product();
		double price = BigDecimal.valueOf(productDTO.getPrice()).setScale(2).doubleValue();
		
		product.setProductName(productDTO.getProductName());
		product.setShopName(productDTO.getShopName());
		product.setCategory(productDTO.getCategory());
		product.setPrice(price);
		product.setUrl(productDTO.getUrl());
		product.setImageUrl(productDTO.getImageUrl());
		
		return product;
	}
}
