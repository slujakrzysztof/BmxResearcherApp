package com.bmxApp.mapper.basketProduct;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.BasketProduct;

public class BasketProductMapper {

	public static BasketProductDTO mapToBasketProductDTO(BasketProduct basketProduct) {
		
		return BasketProductDTO.builder()
				   .productDTO(ProductMapper.mapToProductDTO(basketProduct.getProduct()))
				   .quantity(basketProduct.getQuantity())
				   .shopName(basketProduct.getShopName())
				   .build();
	}
	
	public static BasketProduct mapToBasketProduct(BasketProductDTO basketProductDTO) {
		
		BasketProduct basketProduct = new BasketProduct();
		
		basketProduct.setProduct(ProductMapper.mapToProduct(basketProductDTO.getProductDTO()));
		basketProduct.setQuantity(basketProductDTO.getQuantity());
		basketProduct.setShopName(basketProductDTO.getShopName());
		
		return basketProduct;
	}
}
