package com.bmxApp.mapper.product;

import java.math.RoundingMode;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.formatter.ProductFormatter;
import com.bmxApp.model.product.Product;

@Component
public class ProductMapper implements Function<ProductDTO, Product>{

	@Override
	public Product apply(ProductDTO productDTO) {

		Product product = new Product();
		
		product.setProductName(productDTO.getProductName());
		product.setShopName(productDTO.getShopName());
		product.setCategory(productDTO.getCategory());
		product.setPrice(ProductFormatter.format(productDTO.getPrice()));
		product.setUrl(productDTO.getUrl());
		product.setImageUrl(productDTO.getImageUrl());
		
		return product;
	}
}
