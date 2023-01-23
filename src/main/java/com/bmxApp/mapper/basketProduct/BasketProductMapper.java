package com.bmxApp.mapper.basketProduct;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.basketProduct.BasketProduct;
import com.bmxApp.model.product.Product;

public class BasketProductMapper {

	public static BasketProductDTO mapToBasketProductDTO(BasketProduct basketProduct) {
		
		return BasketProductDTO.builder()
				   .productId(basketProduct.getProduct().getId())
				   .quantity(basketProduct.getQuantity())
				   .shopName(basketProduct.getShopName())
				   .dtoProduct(ProductMapper.mapToProductDTO(basketProduct.getProduct()))
				   .build();
	}
	
	public static BasketProduct mapToBasketProduct(BasketProductDTO basketProductDTO, Product product) {
		
		BasketProduct basketProduct = new BasketProduct();
		
		basketProduct.setProduct(product);
		basketProduct.setQuantity(basketProductDTO.getQuantity());
		basketProduct.setShopName(basketProductDTO.getShopName());
		
		return basketProduct;
	}
}
