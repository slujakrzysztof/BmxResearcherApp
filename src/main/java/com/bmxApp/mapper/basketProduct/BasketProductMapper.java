package com.bmxApp.mapper.basketProduct;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;

public class BasketProductMapper {

	public static BasketProductDTO mapToBasketProductDTO(BasketProduct basketProduct) {
		
		return BasketProductDTO.builder()
				   .productId(basketProduct.getProduct().getId())
				   .productName(basketProduct.getProduct().getProductName())
				   .quantity(basketProduct.getQuantity())
				   .shopName(basketProduct.getShopName())
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
