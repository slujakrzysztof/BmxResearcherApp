package com.bmxApp.mapper.product;

import java.util.function.Function;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.model.product.Product;

public class ProductDTOMapper implements Function<Product, ProductDTO> {

	@Override
	public ProductDTO apply(Product product) {

		return ProductDTO.builder()
				 .productName(product.getProductName())
				 .shopName(product.getShopName())
				 .category(product.getCategory())
				 .price(product.getPrice())
				 .url(product.getUrl())
				 .imageUrl(product.getImageUrl())
				 .build();
	}

}
