package com.bmxApp.dto.product;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ProductDTO {
	private String productName;
	private String shopName;
	private String category;
	private BigDecimal price;
	private String url;
	private String imageUrl;
}
