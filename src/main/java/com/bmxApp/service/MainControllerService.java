package com.bmxApp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.Soundbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.Part;
import com.bmxApp.enums.Shop;
import com.bmxApp.manager.PropertyManager;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.repository.ProductRepository;
import com.bmxApp.researcher.ShopResearcherService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@Getter
@Setter
@NoArgsConstructor
public class MainControllerService {

	@Autowired
	ProductRepositoryService productRepositoryService;

	@Autowired
	BasketProductRepositoryService basketProductRepositoryService;

	//private boolean partSearched = false;
	//private String categoryEnum;
	private String currentShop;
	private String category;

	private String language = "polish";

	//List<Product> products = new ArrayList<Product>();

	@Autowired(required = false)
	ShopResearcherService shopResearcherService;

	public ArrayList<ProductDTO> getProducts(String shopName, String category) {

		return this.productRepositoryService.getSearchedProducts(shopName, category);
	}
	
	public ArrayList<ProductDTO> getProductsWithDiscount(String shopName, String category) {
		
		ArrayList<ProductDTO> productDTOList = this.getProducts(shopName, category);
		DiscountDTO discountDTO = this.getShopResearcherService().getDiscount();
		
		productDTOList.forEach(productDTO -> {
			double discountPrice = productDTO.getPrice() * ((100.0 - discountDTO.getValue()) / 100.0);
			productDTO.setPrice(discountPrice);	
		});
		
		return productDTOList;
	}

	public ArrayList<BasketProductDTO> getBasketProducts() {

		return this.basketProductRepositoryService.getBasketProducts();
	}

	public void search(String category, String shopName, boolean partSelection) {

		String html;

		try {
			PropertyReader.getInstance().connectPropertyReader(shopName);

			if (partSelection)
				html = PropertyManager.getInstance().URL;
			else
				html = PropertyManager.getInstance().SAFETY_URL;

			System.out.println("htmmml: " + html);

			System.out.println("CZESC: " + category);

			shopResearcherService.setConnection(html);
			String partUrl = shopResearcherService.findPartUrl(category);
			shopResearcherService.setConnection(partUrl);

			if (!productRepositoryService.isProductInDatabase(shopName, category))
				shopResearcherService.searchNewProducts(shopName, category, partUrl);

			// shopResearcherService.findPartUrl(null, category)
			// shopResearcher.startSearching(html);
			// shopResearcher.setShopName(shopName);
			// shopResearcher.setConnection();
			// shopResearcher.setCategory(category);

			// shopResearcher.searchPage(); <-- W START SEARCHING
			// shopResearcher.setPagesArray();
			// shopResearcher.startSearching(shopResearcher.getPagesArray().get(0));
			// shopResearcher.setConnection();

			// shopResearcher.setInitialized(true);
			// if (!this.partPreviousSearched(shopName, category)) {
			// shopResearcher.setProductUpdated(false);
			// shopResearcher.searchNewProducts(shopName, category);

			// } else {
			// shopResearcher.setProductUpdated(true);
			// ---- NA RZECZ TESTÓW ----//
			// shopResearcher.searchPreviousProducts(shopName, category);
			// }

			// shopResearcher.setSpecificInformations(category);
			// this.setPartSearched(true);

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	// Iterate all available shops to get products
	public void searchAllShops(String category, boolean partSelection) {

		Arrays.asList(Shop.values()).stream()
				.forEach(shop -> this.search(category, shop.name().toLowerCase(), partSelection));
	}

	/*
	 * public void setPropertyReader(String filename) {
	 * PropertyReader.getInstance().setPropertyFilename("com/bmxApp/properties/" +
	 * filename + ".properties"); System.out.println("Jestem tu: " +
	 * PropertyReader.getInstance().getFilename());
	 * PropertyReader.getInstance().setConnection(); }
	 */

	/*
	 * public boolean wasShopUsed(String shopName) { if
	 * (productDatabaseHandler.findByShopName(shopName).isEmpty()) return false;
	 * return true; }
	 * 
	 * public boolean partPreviousSearched(String shopName, String category) { if
	 * (productDatabaseHandler.findByShopNameAndCategory(shopName,
	 * category).isEmpty()) return false; return true; }
	 */

	public void applyDiscount(List<Product> products, double discountValue) {
		for (int counter = 0; counter < products.size(); counter++) {
			products.get(counter).setPrice(products.get(counter).getPrice() * ((100.0 - discountValue) / 100.0));
			System.out.println("CENA: " + products.get(counter).getPrice());
		}
	}

	public void searchProducts(String shopName, String category) {
		if (shopName.equalsIgnoreCase(Shop.ALLSHOPS.name())) {
			this.searchAllShops(category, true);
		} else {
			this.search(category, shopName.toLowerCase(), true);
		}
	}

	/*
	 * public void setProducts(String shopName, String category) { if
	 * (shopName.equalsIgnoreCase(Shop.ALLSHOPS.name())) {
	 * products.addAll(this.findByCategory(category)); } else {
	 * products.addAll(this.findByCategoryAndShopName(Part.fromString(category).
	 * getValue(shopName), shopName.toLowerCase())); } }
	 */

}
