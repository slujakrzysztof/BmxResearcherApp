package com.bmxApp.service.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.model.product.Product;
import com.bmxApp.service.database.ProductRepositoryService;
import com.bmxApp.service.filter.FilterService;
import com.bmxApp.service.sort.SortService;

@Service
public class DiscountService {


	private final ProductRepositoryService productRepositoryService;
	private final ProductDTOMapper productDtoMapper;
	private final SortService sortService;
	private final FilterService filterService;
	private DiscountDTO discount;

	public DiscountService(ProductRepositoryService productRepositoryService, ProductDTOMapper productDtoMapper,
			SortService sortService, FilterService filterService) {

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

	public void resetDiscount() {

		discount.setValue(0);
	}

	public void applyDiscount(List<ProductDTO> products) {

		BigDecimal discount = new BigDecimal((100.0 - getDiscount()) / 100.0);
		
		products.forEach(product -> product.setPrice(product.getPrice().multiply(discount).setScale(2, RoundingMode.HALF_UP)));
	}

	public void applyBasketDiscount(List<BasketProductDTO> products) {

		BigDecimal discount = new BigDecimal((100.0 - getDiscount()) / 100.0);
		
		products.forEach(product -> product.setPrice(product.getPrice().multiply(discount).setScale(2, RoundingMode.HALF_UP)));
	}

	public List<ProductDTO> getProductsWithDiscount(String shopName, String category) {

		List<ProductDTO> productsDTO;
		List<Product> products = productRepositoryService.getSearchedProducts(shopName, category);

		productsDTO = products.stream().map(product -> productDtoMapper.apply(product)).collect(Collectors.toList());

		this.applyDiscount(productsDTO);
		productsDTO.forEach(product -> product.setPrice(product.getPrice().setScale(2, RoundingMode.HALF_UP)));

		return productsDTO;
	}

	public List<ProductDTO> getProductsWithDiscount(List<ProductDTO> products) {

		List<ProductDTO> discountProducts = products;

		this.applyDiscount(discountProducts);
		discountProducts.forEach(product -> product.setPrice(product.getPrice().setScale(2, RoundingMode.HALF_UP)));
		return products;
	}

	public List<BasketProductDTO> getBasketProductsWithDiscount(List<BasketProductDTO> products) {

		List<BasketProductDTO> discountProducts = products;

		this.applyBasketDiscount(discountProducts);
		discountProducts.forEach(product -> product.setPrice(product.getPrice().setScale(2, RoundingMode.HALF_UP)));
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
		List<ProductDTO> sortedProducts = sortService.sortProductDTO(sortedBy, products, isSorted);

		return sortedProducts;
	}

	public List<ProductDTO> getSortedProductsWithDiscount(String shopName, String category, String sortedBy,
			boolean isSorted) {

		List<ProductDTO> products = this.getProductsWithDiscount(shopName, category);
		List<ProductDTO> sortedProducts = sortService.sortProductDTO(sortedBy, products, isSorted);

		return sortedProducts;
	}

	public List<ProductDTO> getFilteredProductsWithDiscount(String searchValue) {

		List<ProductDTO> filteredProducts = filterService.getFilteredProducts(searchValue);

		this.applyDiscount(filteredProducts);

		return filteredProducts;
	}

}
