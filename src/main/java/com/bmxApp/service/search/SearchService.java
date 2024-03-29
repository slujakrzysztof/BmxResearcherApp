package com.bmxApp.service.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bmxApp.creator.PathCreator;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.Shop;
import com.bmxApp.manager.PropertyManager;
import com.bmxApp.mapper.basketProduct.BasketProductDTOMapper;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.model.product.Product;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.researcher.ShopResearcherService;
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
	private final DiscountService discountService;
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

	public void searchProducts(String shopName, String category) {

		if (shopName.equalsIgnoreCase(Shop.ALLSHOPS.name())) {
			this.searchAllShops(category, true);
		} else {
			this.search(category, shopName.toLowerCase(), true);
		}
	}

	public List<ProductDTO> getProducts(String shopName, String category, 
										String sortBy, String discountValue,
										HttpServletRequest request) {

		Optional<String> sortedBy = Optional.ofNullable(sortBy);
		Optional<String> discount = Optional.ofNullable(discountValue);
		List<ProductDTO> products = getProducts(shopName, category);;
		
		if(sortedBy.isPresent()) {
			
			sortService.setSortedBy(!sortService.isSortedBy());
			products = sortService.sortProductDTO(sortBy, products);
		} else 
			setInitialSearchURL(PathCreator.createSearchUri(request));
		
		if(discount.isPresent()) {
			
			products = discountService.getProductsWithDiscount(products);
		}
		
		return products;
	}

}
