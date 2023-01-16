package com.bmxApp.builder.product;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.model.Product;

public class ProductBuilder {

	public static ProductDTO buildProductDTO(String productName, String shopName, String category, double price,
			String url, String imageUrl) {

			return ProductDTO.builder()
							 .productName(productName)
							 .shopName(shopName)
							 .category(category)
							 .price(price)
							 .url(url)
							 .imageUrl(imageUrl)
							 .build();
	}
	
	public static Product buildProduct(String productName, String shopName, String category, double price,
			String url, String imageUrl) {
		
			return new Product(
						  productName,
						  shopName,
						  category,
						  price,
						  url,
						  imageUrl);
	}
}
