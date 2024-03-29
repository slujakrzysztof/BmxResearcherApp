package com.bmxApp.mapper.product;

import java.math.RoundingMode;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.formatter.ProductFormatter;
import com.bmxApp.model.product.Product;

@Component
public class ProductDTOMapper implements Function<Product, ProductDTO> {

	@Override
	public ProductDTO apply(Product product) {

		return ProductDTO.builder()
				 .productName(product.getProductName())
				 .shopName(product.getShopName())
				 .category(product.getCategory())
				 .price(ProductFormatter.format(product.getPrice()))
				 .url(product.getUrl())
				 .imageUrl(product.getImageUrl())
				 .build();
	}

}
