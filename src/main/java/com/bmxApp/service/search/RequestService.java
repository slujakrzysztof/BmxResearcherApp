package com.bmxApp.service.search;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.model.product.Product;
import com.bmxApp.service.database.ProductRepositoryService;
import com.bmxApp.service.sort.SortService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestService {

	private final ProductRepositoryService productRepositoryService;
	private final ProductDTOMapper productDTOMapper;
	private final SortService sortService;
	
	public List<ProductDTO> getRequestedProducts(String value) {

		List<Product> products = productRepositoryService.getRequestedItem(value);
		List<ProductDTO> productsDTO;

		productsDTO = products.stream().map(product -> productDTOMapper.apply(product)).collect(Collectors.toList());

		return productsDTO;
	}

	public List<ProductDTO> getSortedRequestedProducts(String value, String sortedBy,
			boolean isSorted) {

		List<ProductDTO> products = this.getRequestedProducts(value);
		List<ProductDTO> sortedProducts = sortService.sortProductDTO(sortedBy, products);

		if (!isSorted)
			Collections.reverse(sortedProducts);

		return sortedProducts;
	}
	
	public String getSearchURL(HttpServletRequest request) {

		return request.getRequestURI() + "?" + request.getQueryString();
	}
}
