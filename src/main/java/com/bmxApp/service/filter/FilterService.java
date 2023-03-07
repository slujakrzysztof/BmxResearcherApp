package com.bmxApp.service.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.formatter.product.ProductFormatter;
import com.bmxApp.mapper.basketProduct.BasketProductDTOMapper;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.model.basketProduct.BasketProduct;
import com.bmxApp.model.product.Product;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.database.BasketProductRepositoryService;
import com.bmxApp.service.database.ProductRepositoryService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@Getter
@RequiredArgsConstructor
public class FilterService {

	private final ProductRepositoryService productRepositoryService;
	private final BasketProductRepositoryService basketProductRepositoryService;
	private final ShopResearcherService shopResearcherService;
	private final ProductDTOMapper productDTOMapper;
	private final BasketProductDTOMapper basketProductDTOMapper;

	public List<ProductDTO> getFilteredItemsWithDiscountByShopName(String value, String shopName, DiscountDTO discount) {

		List<Product> products = productRepositoryService.getRequestedItem(value);
		List<ProductDTO> productsDTO = new LinkedList<>();

		productsDTO = products.stream().filter(product -> product.getShopName().equalsIgnoreCase(shopName))
				.map(product -> productDTOMapper.apply(product)).collect(Collectors.toList());
		
		productsDTO.forEach(productDTO -> productDTO.setPrice(Double.parseDouble(
				ProductFormatter.formatProductPrice(productDTO.getPrice() * ((100.0 - discount.getValue()) / 100.0)))));

		return productsDTO;
	}
	
	public List<BasketProductDTO> getBasketProducts() {

		List<BasketProduct> basketProducts = basketProductRepositoryService.getBasketProducts();
		List<BasketProductDTO> basketProductsDTO = new ArrayList<>();

		basketProductsDTO = basketProducts.stream().map(basketProduct -> basketProductDTOMapper.apply(basketProduct))
				.collect(Collectors.toList());

		return basketProductsDTO;
	}
	
	public double getBasketTotalPrice() {
		
		return this.basketProductRepositoryService.getTotalPrice();
	}
}
