package com.bmxApp.dto.product;

import java.math.BigDecimal;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class CompareProductDTO {

	private String productName;
	private String shopName;
	private BigDecimal price;
	@Nullable
	private String description;
	@Nullable
	private String uri;
	
}
