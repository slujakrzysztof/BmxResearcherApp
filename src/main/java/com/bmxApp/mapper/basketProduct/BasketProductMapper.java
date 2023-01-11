package com.bmxApp.mapper.basketProduct;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.BasketProduct;

public class BasketProductMapper {

	public static BasketProductDTO mapToBasketProductDTO(BasketProduct basketProduct) {
		return BasketProductDTO.builder()
				.id(basketProduct.getId())
				.productDTO(ProductMapper.mapToProductDTO(basketProduct.getProduct()))
				.quantity(basketProduct.getQuantity())
				.shopName(basketProduct.getShopName())
				.build();
	}
	
	public static BasketProduct mapToBasketProduct(BasketProductDTO basketProductDTO) {
		return BasketProduct.builder()
				.id(basketProductDTO.getId())
				.product(ProductMapper.mapToProduct(basketProductDTO.getProductDTO()))
				.quantity(basketProductDTO.getQuantity())
				.shopName(basketProductDTO.getShopName())
				.build();
	}
}
