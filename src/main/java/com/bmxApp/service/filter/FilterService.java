package com.bmxApp.service.filter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.mapper.basketProduct.BasketProductDTOMapper;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.model.product.Product;
import com.bmxApp.service.database.BasketProductRepositoryService;
import com.bmxApp.service.database.ProductRepositoryService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class FilterService {

	private final ProductRepositoryService productRepositoryService;
	private final BasketProductRepositoryService basketProductRepositoryService;
	private final ProductDTOMapper productDTOMapper;
	private final BasketProductDTOMapper basketProductDTOMapper;

	@Value("0")
	private Integer minPrice;
	@Value("0")
	private Integer maxPrice;
	private String shop;
	private String category;

	public List<ProductDTO> getFilteredProducts(String searchValue) {

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
			productsDTO = productsDTO.stream()
					.filter(product -> product.getPrice().compareTo(new BigDecimal(minimumPrice.get().intValue())) == 1)
					.collect(Collectors.toList());

		if (maximumPrice.isPresent())
			productsDTO = productsDTO.stream().filter(product -> product.getPrice().compareTo(new BigDecimal(maximumPrice.get().intValue())) == -1)
					.collect(Collectors.toList());

		return productsDTO;
	}

}
