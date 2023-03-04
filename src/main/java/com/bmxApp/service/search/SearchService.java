package com.bmxApp.service.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Lists;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.Shop;
import com.bmxApp.enums.SortingItem;
import com.bmxApp.formatter.product.ProductFormatter;
import com.bmxApp.manager.PropertyManager;
import com.bmxApp.mapper.basketProduct.BasketProductDTOMapper;
import com.bmxApp.mapper.basketProduct.BasketProductMapper;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.basketProduct.BasketProduct;
import com.bmxApp.model.product.Product;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.basketProduct.BasketProductRepositoryService;
import com.bmxApp.service.discount.DiscountService;
import com.bmxApp.service.product.ProductRepositoryService;
import com.bmxApp.service.sort.SortService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class SearchService {

	private final ProductRepositoryService productRepositoryService;
	private final BasketProductRepositoryService basketProductRepositoryService;
	private final ShopResearcherService shopResearcherService;
	private final SortService sortService;
	private final DiscountService discountService;
	private final ProductDTOMapper productDTOMapper;
	private final BasketProductDTOMapper basketProductDTOMapper;

	@Value("false")
	private boolean sortedBy;

	public List<Product> getProducts(String shopName, String category) {

		return this.productRepositoryService.getSearchedProducts(shopName, category);
	}

	public List<ProductDTO> getRequestedItemsWithDiscount(String value, DiscountDTO discount) {

		List<Product> products = productRepositoryService.getRequestedItem(value);
		List<ProductDTO> productsDTO = new LinkedList<>();

		productsDTO = products.stream().map(product -> productDTOMapper.apply(product)).collect(Collectors.toList());
		productsDTO.forEach(productDTO -> productDTO.setPrice(Double.parseDouble(
				ProductFormatter.formatProductPrice(productDTO.getPrice() * ((100.0 - discount.getValue()) / 100.0)))));

		return productsDTO;
	}
	
	public List<ProductDTO> getSortedRequestedItemsWithDiscount(String value, DiscountDTO discount, String sortedBy, boolean isSorted) {

		List<ProductDTO> products = this.getRequestedItemsWithDiscount(value, discount);
		List<ProductDTO> sortedProducts = sortService.sortProductDTO(sortedBy, products);
		
		if(!isSorted) Collections.reverse(sortedProducts);
		
		return sortedProducts;
	}

	public void search(String category, String shopName, boolean partSelection) {

		String html;

		try {
			PropertyReader.getInstance().connectPropertyReader(shopName);

			if (partSelection)
				html = PropertyManager.getInstance().URL();
			else
				html = PropertyManager.getInstance().SAFETY_URL();

			shopResearcherService.setConnection(html);
			String partUrl = shopResearcherService.findPartUrl(category);
			shopResearcherService.setConnection(partUrl);

			if (!productRepositoryService.areCategoryAndShopNameInDatabase(shopName, category))
				shopResearcherService.searchNewProducts(shopName, category, partUrl);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	// Iterate all available shops to get products
	public void searchAllShops(String category, boolean partSelection) {

		Shop.getShops().stream().forEach(shop -> this.search(category, shop.name().toLowerCase(), partSelection));
	}

	public String getSearchURL(HttpServletRequest request) {

		return request.getRequestURL() + "?" + request.getQueryString();
	}

	public List<BasketProductDTO> getBasketProducts() {

		List<BasketProduct> basketProducts = basketProductRepositoryService.getBasketProducts();
		List<BasketProductDTO> basketProductsDTO = new ArrayList<>();

		basketProductsDTO = basketProducts.stream().map(basketProduct -> basketProductDTOMapper.apply(basketProduct))
				.collect(Collectors.toList());

		return basketProductsDTO;
	}

	public List<ProductDTO> getSortedProductsWithDiscount(String shopName, String category, String sortedBy, boolean isSorted) {

		List<ProductDTO> products = this.getProductsWithDiscount(shopName, category);
		List<ProductDTO> sortedProducts = sortService.sortProductDTO(sortedBy, products);

		if(!isSorted) Collections.reverse(sortedProducts);
		
		return sortedProducts;
	}

	public int getBasketAmount() {
		return this.getBasketProducts().size();
	}

	public double getBasketTotalPrice() {
		return this.basketProductRepositoryService.getTotalPrice();
	}

	public void searchProducts(String shopName, String category) {

		if (shopName.equalsIgnoreCase(Shop.ALLSHOPS.name())) {
			this.searchAllShops(category, true);
		} else {
			this.search(category, shopName.toLowerCase(), true);
		}
	}
}
