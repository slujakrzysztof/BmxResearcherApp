package com.bmxApp.service.search;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.model.product.Product;
import com.bmxApp.service.database.ProductRepositoryService;
import com.bmxApp.service.discount.DiscountService;
import com.bmxApp.service.sort.SortService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestService {

	private final ProductRepositoryService productRepositoryService;
	private final ProductDTOMapper productDTOMapper;
	private final SortService sortService;
	private final DiscountService discountService;
	
	public List<ProductDTO> getRequestedProducts(String value) {

		List<Product> products = productRepositoryService.getRequestedItem(value);
		List<ProductDTO> productsDTO;

		productsDTO = products.stream().map(product -> productDTOMapper.apply(product)).collect(Collectors.toList());

		return productsDTO;
	}
	
	public List<ProductDTO> getProducts(String searchValue, String sortBy,
										String discountValue){
		
		Optional<String> sortedBy = Optional.ofNullable(sortBy);
		Optional<String> discount = Optional.ofNullable(discountValue);
		
		List<ProductDTO> products = getRequestedProducts(searchValue);
		
		if(sortedBy.isPresent()) {
			
			sortService.setSortedBy(!sortService.isSortedBy());
			products = sortService.sortProductDTO(sortBy, products);
		}
		
		if(discount.isPresent()) products = discountService.getProductsWithDiscount(products);
		
		return null;
	}
	

}
