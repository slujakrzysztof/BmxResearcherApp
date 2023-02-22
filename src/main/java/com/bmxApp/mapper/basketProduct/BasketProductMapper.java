package com.bmxApp.mapper.basketProduct;

import java.util.function.Function;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.basketProduct.BasketProduct;
import com.bmxApp.model.product.Product;
import com.bmxApp.service.product.ProductRepositoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasketProductMapper implements Function<BasketProductDTO, BasketProduct> {

	final ProductRepositoryService productRepositoryService;

	@Override
	public BasketProduct apply(BasketProductDTO basketProductDTO) {

		BasketProduct basketProduct = new BasketProduct();
		Product product = productRepositoryService.getProductByProductNameAndShopName(basketProductDTO.getProductName(),
				basketProductDTO.getShopName());

		basketProduct.setProduct(product);
		basketProduct.setQuantity(basketProductDTO.getQuantity());
		basketProduct.setShopName(basketProductDTO.getShopName());

		return basketProduct;
	}
}
