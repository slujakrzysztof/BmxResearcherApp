package com.bmxApp.builder.basketProduct;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.product.ProductDTO;

public class BasketProductBuilder {

	public static BasketProductDTO buildBasketProduct(ProductDTO productDTO, int quantity, String shopName) {
		return BasketProductDTO.builder()
							   .productDTO(productDTO)
							   .quantity(quantity)
							   .shopName(shopName)
							   .build();
	}
	
}
