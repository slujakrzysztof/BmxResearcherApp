package com.bmxApp.mapper.product;

import java.math.BigDecimal;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.model.Product;

public class ProductMapper {

	public static ProductDTO mapToProductDTO(Product product) {
		
		return ProductDTO.builder()
				 .productName(product.getProductName())
				 .shopName(product.getShopName())
				 .category(product.getCategory())
				 .price(product.getPrice())
				 .url(product.getUrl())
				 .imageUrl(product.getImageUrl())
				 .build();
		
	}

	public static Product mapToProduct(ProductDTO productDTO) {
		
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
