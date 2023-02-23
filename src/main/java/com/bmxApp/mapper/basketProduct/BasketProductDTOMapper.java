package com.bmxApp.mapper.basketProduct;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.model.basketProduct.BasketProduct;

@Component 
public class BasketProductDTOMapper implements Function<BasketProduct, BasketProductDTO> {

	@Override
	public BasketProductDTO apply(BasketProduct basketProduct) {

		return BasketProductDTO.builder()
				   .productId(basketProduct.getProduct().getId())
				   .quantity(basketProduct.getQuantity())
				   .shopName(basketProduct.getShopName())
				   .imageUrl(basketProduct.getProduct().getImageUrl())
				   .url(basketProduct.getProduct().getUrl())
				   .price(basketProduct.getProduct().getPrice())
				   .productName(basketProduct.getProduct().getProductName())
				   .build();
	}

}
