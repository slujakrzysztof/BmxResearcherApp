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
import com.bmxApp.service.discount.DiscountService;
import com.bmxApp.service.sort.SortService;

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
	private final SortService sortService;
	private final DiscountService discountService;
	
	@Value("0")
	private Integer minPrice;
	@Value("0")
	private Integer maxPrice;
	private String shop;
	private String category;

	
	public void setParameters(int minPrice, int maxPrice, String category, String shopName) {
		
		setMinPrice(minPrice);
		setMaxPrice(maxPrice);
		setCategory(category);
		setShop(shopName);
	}
	
	public List<ProductDTO> getFilteredProducts(String searchValue, 
												String discountValue,
												String sortBy) {

		List<Product> products = productRepositoryService.getRequestedItem(searchValue);
		List<ProductDTO> productsDTO;
		Optional<Integer> minimumPrice = Optional.ofNullable(minPrice);
		Optional<Integer> maximumPrice = Optional.ofNullable(maxPrice);
		Optional<String> shopName = Optional.ofNullable(shop);
		Optional<String> categoryName = Optional.ofNullable(category);
		//Optional<String> discount = Optional.ofNullable(discountValue);
		
		
		
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
		
		if(sortBy != null) {
			
			sortService.setSortedBy(!sortService.isSortedBy());
			productsDTO = sortService.sortProductDTO(sortBy, productsDTO);
		}
		
		if(discountValue != null)
			productsDTO = discountService.getProductsWithDiscount(productsDTO);

		return productsDTO;
	}

}

