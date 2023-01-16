package com.bmxApp.builder.basketProduct;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;

public class BasketProductBuilder {

	public static BasketProductDTO buildBasketProductDTO(ProductDTO productDTO, int quantity, String shopName) {
		
		return BasketProductDTO.builder()
							   .productDTO(productDTO)
							   .quantity(quantity)
							   .shopName(shopName)
							   .build();
	}
	
	public static BasketProduct buildBasketProduct(Product product, 
												   int quantity, 
												   String shopName) {
		
		return new BasketProduct(product,
								 quantity,
								 shopName);
	}
	
}
