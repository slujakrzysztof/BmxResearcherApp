package com.bmxApp.service.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.Shop;
import com.bmxApp.manager.PropertyManager;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.product.Product;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.basketProduct.BasketProductRepositoryService;
import com.bmxApp.service.product.ProductRepositoryService;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class SearchService {

	@Autowired
	ProductRepositoryService productRepositoryService;

	@Autowired
	BasketProductRepositoryService basketProductRepositoryService;
	
	@Autowired(required = false)
	ShopResearcherService shopResearcherService;
	
	private String currentShop;
	private String category;

	public ArrayList<Product> getProducts(String shopName, String category) {

		return this.productRepositoryService.getSearchedProducts(shopName, category);
	}

	public ArrayList<ProductDTO> getProductsWithDiscount(String shopName, String category) {

		ArrayList<Product> productList = this.getProducts(shopName, category);
		DiscountDTO discountDTO = this.getShopResearcherService().getDiscount();
		ArrayList<ProductDTO> productDTOList = new ArrayList<>();
		
		productList.forEach(product -> {

			ProductDTO dtoProduct = ProductMapper.mapToProductDTO(product);
			dtoProduct.setPrice(product.getPrice() * ((100.0 - discountDTO.getValue()) / 100.0));
			productDTOList.add(dtoProduct);
		});

		return productDTOList;
	}
	

	public void applyDiscount(List<Product> products, double discountValue) {
		
		for (int counter = 0; counter < products.size(); counter++) 
			products.get(counter).setPrice(products.get(counter).getPrice() * ((100.0 - discountValue) / 100.0));
	}
	
	public void setDiscount(int value) {
		
		shopResearcherService.getDiscount().setValue(value);
	}
	
	public void resetDiscount() {
		
		shopResearcherService.getDiscount().setValue(0);
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

		Arrays.asList(Shop.values()).stream()
				.forEach(shop -> this.search(category, shop.name().toLowerCase(), partSelection));
	}

	public void searchProducts(String shopName, String category) {
		
		if (shopName.equalsIgnoreCase(Shop.ALLSHOPS.name())) {
			this.searchAllShops(category, true);
		} else {
			this.search(category, shopName.toLowerCase(), true);
		}
	}
}
