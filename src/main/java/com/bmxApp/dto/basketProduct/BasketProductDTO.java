package com.bmxApp.dto.basketProduct;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasketProductDTO {
	//private ProductDTO productDTO;
	private int productId;
	private String productName;
	private int quantity;
	private String shopName;
}
