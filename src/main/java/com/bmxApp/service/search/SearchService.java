package com.bmxApp.service.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.Shop;
import com.bmxApp.manager.PropertyManager;
import com.bmxApp.mapper.basketProduct.BasketProductMapper;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.basketProduct.BasketProduct;
import com.bmxApp.model.product.Product;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.basketProduct.BasketProductRepositoryService;
import com.bmxApp.service.product.ProductRepositoryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class SearchService {

	private final ProductRepositoryService productRepositoryService;
	private final BasketProductRepositoryService basketProductRepositoryService;
	private final ShopResearcherService shopResearcherService;
	
	private String currentShop;
	private String category;
	
	public SearchService(ProductRepositoryService productRepositoryService,
						 BasketProductRepositoryService basketProductRepositoryService,
						 ShopResearcherService shopResearcherService) {
		
		this.productRepositoryService = productRepositoryService;
		this.basketProductRepositoryService = basketProductRepositoryService;
		this.shopResearcherService = shopResearcherService;		
	}

	public ArrayList<Product> getProducts(String shopName, String category) {

		return this.productRepositoryService.getSearchedProducts(shopName, category);
	}

	public ArrayList<ProductDTO> getProductsWithDiscount(String shopName, String category) {

		ArrayList<Product> productList = this.getProducts(shopName, category);
		DiscountDTO discountDTO = this.getShopResearcherService().getDiscount();
		ArrayList<ProductDTO> productDTOList = new ArrayList<>();
		
		productList.forEach(product -> {

			ProductDTO dtoProduct = ProductMapper.mapToProductDTO(product);
			double price = Double.parseDouble(this.formatPrice(product.getPrice() * ((100.0 - discountDTO.getValue()) / 100.0)));
			dtoProduct.setPrice(price);
			productDTOList.add(dtoProduct);
		});

		return productDTOList;
	}
	
	public String formatPrice(double price) {
		return String.format(Locale.US, "%.2f", price);
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
	
	public String getSearchURL(HttpServletRequest request) {
		
		return request.getRequestURL() + "?" + request.getQueryString();
	}
	
	public ArrayList<BasketProductDTO> getBasketProducts() {
		
		ArrayList<BasketProduct> basketProducts = basketProductRepositoryService.getBasketProducts();
		ArrayList<BasketProductDTO> dtoBasketProducts = new ArrayList<>();
		
		basketProducts.forEach(basketProduct -> {
			BasketProductDTO dtoBasketProduct = BasketProductMapper.mapToBasketProductDTO(basketProduct);
			dtoBasketProducts.add(dtoBasketProduct);
		});
		
		return dtoBasketProducts;
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
