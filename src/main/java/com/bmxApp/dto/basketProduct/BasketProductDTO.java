package com.bmxApp.dto.basketProduct;

import com.bmxApp.dto.product.ProductDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasketProductDTO {
	
	private int productId;
	private int quantity;
	private ProductDTO dtoProduct;
	private String shopName;
}
