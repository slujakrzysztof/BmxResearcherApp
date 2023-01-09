package com.bmxApp.mapper.product;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.model.Product;

public class ProductMapper {

	public static ProductDTO mapToProductDTO(Product product) {
		return ProductDTO.builder()
				.productName(product.getProductName())
				.shopName(product.getShopName())
				.category(product.getCategory())
				.categoryEnum(product.getCategoryEnum())
				.price(product.getPrice())
				.url(product.getUrl())
				.imageUrl(product.getImageUrl())
				.build();
	}
}
