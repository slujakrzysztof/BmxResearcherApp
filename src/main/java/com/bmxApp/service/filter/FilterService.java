package com.bmxApp.service.filter;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.FilterItem;
import com.bmxApp.mapper.basketProduct.BasketProductDTOMapper;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.model.product.Product;
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
	private final ProductDTOMapper productDTOMapper;
	private final BasketProductDTOMapper basketProductDTOMapper;

	public List<ProductDTO> getFilteredProducts(String searchValue, String shop, String category, Integer minPrice,
			Integer maxPrice) {

		List<Product> products = productRepositoryService.getRequestedItem(searchValue);
		List<ProductDTO> productsDTO;
		Optional<Integer> minimumPrice = Optional.ofNullable(minPrice);
		Optional<Integer> maximumPrice = Optional.ofNullable(maxPrice);
		Optional<String> shopName = Optional.ofNullable(shop);
		Optional<String> categoryName = Optional.ofNullable(category);

		productsDTO = products.stream().map(product -> productDTOMapper.apply(product)).collect(Collectors.toList());

		if(shopName.isPresent())
			productsDTO = productsDTO.stream().filter(product -> product.getShopName().equalsIgnoreCase(shop))
			.collect(Collectors.toList());

		if(categoryName.isPresent())
			productsDTO = productsDTO.stream().filter(product -> product.getCategory().equalsIgnoreCase(category))
			.collect(Collectors.toList());
		
		if (minimumPrice.isPresent())
			productsDTO = productsDTO.stream().filter(product -> product.getPrice() > minimumPrice.get().intValue())
					.collect(Collectors.toList());

		if (maximumPrice.isPresent())
			productsDTO = productsDTO.stream().filter(product -> product.getPrice() < maximumPrice.get().intValue())
					.collect(Collectors.toList());

		return productsDTO;
	}

}
