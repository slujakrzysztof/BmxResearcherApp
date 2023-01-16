package com.bmxApp.mapper.basketProduct;

import com.bmxApp.builder.basketProduct.BasketProductBuilder;
import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.BasketProduct;

public class BasketProductMapper {

	public static BasketProductDTO mapToBasketProductDTO(BasketProduct basketProduct) {
		
		return BasketProductBuilder.buildBasketProductDTO(ProductMapper.mapToProductDTO(basketProduct.getProduct()), 
														  basketProduct.getQuantity(), 
														  basketProduct.getShopName());
	}
	
	public static BasketProduct mapToBasketProduct(BasketProductDTO basketProductDTO) {
		
		return BasketProductBuilder.buildBasketProduct(ProductMapper.mapToProduct(basketProductDTO.getProductDTO()), 
													   basketProductDTO.getQuantity(), 
													   basketProductDTO.getShopName());
	}
}
