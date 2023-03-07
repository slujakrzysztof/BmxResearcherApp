package com.bmxApp.service.discount;

import java.util.Collections;
import java.util.List; 
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.formatter.product.ProductFormatter;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.model.product.Product;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.product.ProductRepositoryService;
import com.bmxApp.service.search.SearchService;
import com.bmxApp.service.sort.SortService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscountService {

	private final ShopResearcherService shopResearcherService;
	private final ProductRepositoryService productRepositoryService;
	private final ProductDTOMapper productDtoMapper;
	private final SortService sortService;

	public void applyDiscount(List<ProductDTO> products, int discountValue) {

		products.forEach(product -> {

			double price = product.getPrice() * ((100.0 - discountValue) / 100.0);
			product.setPrice(Double.parseDouble(ProductFormatter.formatProductPrice(price)));
		});
	}

	public void setDiscount(int value) {

		shopResearcherService.getDiscount().setValue(value);
	}

	public void resetDiscount() {

		shopResearcherService.getDiscount().setValue(0);
	}

	public int getDiscount() {

		return this.shopResearcherService.getDiscount().getValue();
	}

	public List<ProductDTO> getProductsWithDiscount(String shopName, String category) {

		List<ProductDTO> productsDTO;
		List<Product> products = productRepositoryService.getSearchedProducts(shopName, category);
		DiscountDTO discountDTO = shopResearcherService.getDiscount();

		productsDTO = products.stream().map(product -> productDtoMapper.apply(product)).collect(Collectors.toList());

		this.applyDiscount(productsDTO, discountDTO.getValue());
		productsDTO.forEach(product -> product
				.setPrice(Double.parseDouble(ProductFormatter.formatProductPrice(product.getPrice()))));

		return productsDTO;
	}

	public List<ProductDTO> getRequestedProductsWithDiscount(String value) {

		List<Product> products = productRepositoryService.getRequestedItem(value);
		List<ProductDTO> productsDTO;

		productsDTO = products.stream().map(product -> productDtoMapper.apply(product)).collect(Collectors.toList());
		
		this.applyDiscount(productsDTO, this.getDiscount());

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

	
}
