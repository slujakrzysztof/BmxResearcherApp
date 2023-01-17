package com.bmxApp.mapper.product;

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
		
		product.setProductName(productDTO.getProductName());
		product.setShopName(productDTO.getShopName());
		product.setCategory(productDTO.getCategory());
		product.setPrice(productDTO.getPrice());
		product.setUrl(productDTO.getUrl());
		product.setImageUrl(productDTO.getImageUrl());
		
		return product;
		
	}
}
