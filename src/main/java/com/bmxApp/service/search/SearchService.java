package com.bmxApp.service.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Lists;

import com.bmxApp.creator.PathCreator;
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
import com.bmxApp.service.database.BasketProductRepositoryService;
import com.bmxApp.service.database.ProductRepositoryService;
import com.bmxApp.service.discount.DiscountService;
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
	private final ShopResearcherService shopResearcherService;
	private final SortService sortService;
	private final ProductDTOMapper productDTOMapper;
	private final BasketProductDTOMapper basketProductDTOMapper;
	
	@Value("/main")
	private String initialSearchURL;

	public List<ProductDTO> getProducts(String shopName, String category) {

		List<Product> products = productRepositoryService.getSearchedProducts(shopName, category);
		List<ProductDTO> productsDTO;

		productsDTO = products.stream().map(product -> productDTOMapper.apply(product)).collect(Collectors.toList());

		return productsDTO;
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


	public List<ProductDTO> getSortedProducts(String shopName, String category, String sortedBy,
			boolean isSorted) {

		List<ProductDTO> products = this.getProducts(shopName, category);
		List<ProductDTO> sortedProducts = sortService.sortProductDTO(sortedBy, products, isSorted);

		return sortedProducts;
	}

	public void searchProducts(String shopName, String category) {

		if (shopName.equalsIgnoreCase(Shop.ALLSHOPS.name())) {
			this.searchAllShops(category, true);
		} else {
			this.search(category, shopName.toLowerCase(), true);
		}
	}
	
	public Map<String, String> getModelAttributes(String url){
		
		String params = url.substring(url.indexOf("?") + 1, url.length());
		String[] list = params.split("&");
		String[] elements = new String[2];
		Map<String, String> paramsMap = new HashMap<>();
		
		for(int i=0; i < list.length; i++) {
			elements = list[i].split("=");
			paramsMap.put(elements[0], elements[1]);
		}
			
		return paramsMap;
	}
}
