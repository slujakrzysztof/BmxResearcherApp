package com.bmxApp.dto.basketProduct;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.model.Product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasketProductDTO {
	private Integer id;
	private ProductDTO product;
	private int quantity;
	private String shopName;
}
