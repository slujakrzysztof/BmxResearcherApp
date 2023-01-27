package com.bmxApp.dto.product;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ProductDTO {
	private String productName;
	private String shopName;
	private String category;
	private double price;
	private String url;
	private String imageUrl;
}
