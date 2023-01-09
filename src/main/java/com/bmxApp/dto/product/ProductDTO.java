package com.bmxApp.dto.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDTO {
	private String productName;
	private String shopName;
	private String category;
	private String categoryEnum;
	private double price;
	private String url;
	private String imageUrl;
}
