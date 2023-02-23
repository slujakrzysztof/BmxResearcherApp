package com.bmxApp.dto.basketProduct;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasketProductDTO {
	
	private int productId;
	private int quantity;
	private String imageUrl;
	private String url;
	private String category;
	private String productName;
	private double price;
	private String shopName;
}
