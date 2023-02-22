package com.bmxApp.mapper.basketProduct;

import java.util.function.Function;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.basketProduct.BasketProduct;

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
