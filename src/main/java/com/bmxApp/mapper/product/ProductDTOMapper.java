package com.bmxApp.mapper.product;

import java.math.RoundingMode;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.model.product.Product;

@Component
public class ProductDTOMapper implements Function<Product, ProductDTO> {

	@Override
	public ProductDTO apply(Product product) {

		return ProductDTO.builder()
				 .productName(product.getProductName())
				 .shopName(product.getShopName())
				 .category(product.getCategory())
				 .price(product.getPrice().setScale(2,RoundingMode.HALF_UP))
				 .url(product.getUrl())
				 .imageUrl(product.getImageUrl())
				 .build();
	}

}
