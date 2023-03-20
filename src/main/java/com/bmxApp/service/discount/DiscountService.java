package com.bmxApp.service.discount;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.formatter.product.ProductFormatter;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.model.product.Product;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.database.ProductRepositoryService;
import com.bmxApp.service.filter.FilterService;
import com.bmxApp.service.sort.SortService;

import lombok.RequiredArgsConstructor;

@Service
public class DiscountService {

	private final String DISCOUNT_KEY;

	private final ProductRepositoryService productRepositoryService;
	private final ProductDTOMapper productDtoMapper;
	private final SortService sortService;
	private final FilterService filterService;
	private DiscountDTO discount;

	public DiscountService(ProductRepositoryService productRepositoryService, ProductDTOMapper productDtoMapper,
			SortService sortService, FilterService filterService) {

		this.DISCOUNT_KEY = "discountValue=";
		this.productRepositoryService = productRepositoryService;
		this.productDtoMapper = productDtoMapper;
		this.sortService = sortService;
		this.filterService = filterService;
		this.discount = new DiscountDTO();
	}

	public void setDiscount(int discount) {

		this.discount.setValue(discount);
	}

	public int getDiscount() {

		return this.discount.getValue();
	}

	public void applyDiscount(List<ProductDTO> products) {

		products.forEach(product -> {

			double price = product.getPrice() * ((100.0 - getDiscount()) / 100.0);
			product.setPrice(Double.parseDouble(ProductFormatter.formatProductPrice(price)));
		});
	}

	public String createUrlWithDiscount(String url, String discountValue) {

		int discount = Integer.parseInt(discountValue);
		String requestUrl = url.substring(0, url.indexOf("?") + 1);
		String params = url.substring(url.indexOf("?") + 1);
		StringBuilder finalUrl = new StringBuilder(requestUrl);
		int index = 0;

		List<String> paramList = new LinkedList<>(Arrays.asList(params.split("&")));

		if (params.contains(DISCOUNT_KEY)) {
		
			for (String param : paramList)
				if (param.contains(DISCOUNT_KEY))
					index = paramList.indexOf(param);
		
			paramList.remove(index);
			
		}
		
		

		for (String param : paramList) {
			if (!(paramList.indexOf(param) == 0))
				finalUrl.append("&");
			finalUrl.append(param);
		}

		if (!(discount == 0)) {
			finalUrl.append("&");
			finalUrl.append(DISCOUNT_KEY).append(discount);
		}

		return finalUrl.toString();

	}

	public List<ProductDTO> getProductsWithDiscount(String shopName, String category) {

		List<ProductDTO> productsDTO;
		List<Product> products = productRepositoryService.getSearchedProducts(shopName, category);

		productsDTO = products.stream().map(product -> productDtoMapper.apply(product)).collect(Collectors.toList());

		this.applyDiscount(productsDTO);
		productsDTO.forEach(product -> product
				.setPrice(Double.parseDouble(ProductFormatter.formatProductPrice(product.getPrice()))));

		return productsDTO;
	}

	public List<ProductDTO> getProductsWithDiscount(List<ProductDTO> products) {

		List<ProductDTO> discountProducts = products;

		this.applyDiscount(discountProducts);
		discountProducts.forEach(product -> product
				.setPrice(Double.parseDouble(ProductFormatter.formatProductPrice(product.getPrice()))));
		return products;
	}

	public List<ProductDTO> getRequestedProductsWithDiscount(String value) {

		List<Product> products = productRepositoryService.getRequestedItem(value);
		List<ProductDTO> productsDTO;

		productsDTO = products.stream().map(product -> productDtoMapper.apply(product)).collect(Collectors.toList());

		this.applyDiscount(productsDTO);

		return productsDTO;
	}

	public List<ProductDTO> getSortedRequestedProductsWithDiscount(String value, String sortedBy, boolean isSorted) {

		List<ProductDTO> products = this.getRequestedProductsWithDiscount(value);
		List<ProductDTO> sortedProducts = sortService.sortProductDTO(sortedBy, products);

		if (!isSorted)
			Collections.reverse(sortedProducts);

		return products;
	}

	public List<ProductDTO> getSortedProductsWithDiscount(String shopName, String category, String sortedBy,
			boolean isSorted) {

		List<ProductDTO> products = this.getProductsWithDiscount(shopName, category);
		List<ProductDTO> sortedProducts = sortService.sortProductDTO(sortedBy, products);

		if (!isSorted)
			Collections.reverse(sortedProducts);

		return products;
	}

	public List<ProductDTO> getFilteredProductsWithDiscount(String searchValue) {

		List<ProductDTO> filteredProducts = filterService.getFilteredProducts(searchValue);

		this.applyDiscount(filteredProducts);

		return filteredProducts;
	}

}
