package com.bmxApp.mapper.product;

import com.bmxApp.builder.product.ProductBuilder;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.model.Product;

public class ProductMapper {

	public static ProductDTO mapToProductDTO(Product product) {
		return ProductBuilder.buildProductDTO(product.getProductName(), 
											  product.getShopName(), 
											  product.getCategory(),
											  product.getPrice(), 
											  product.getUrl(), 
											  product.getImageUrl());
		
	}

	public static Product mapToProduct(ProductDTO productDTO) {
		return ProductBuilder.buildProduct(productDTO.getProductName(), 
										   productDTO.getShopName(), 
										   productDTO.getCategory(),
										   productDTO.getPrice(), 
										   productDTO.getUrl(), 
										   productDTO.getImageUrl());
	}
}
