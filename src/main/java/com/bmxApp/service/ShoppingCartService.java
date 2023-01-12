package com.bmxApp.service;

import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;
import com.bmxApp.repository.BasketProductRepository;
import com.bmxApp.researcher.ShopResearcherService;

@Service
public class ShoppingCartService {

	@Autowired
	private BasketProductDatabaseService basketProductDatabaseService;

	@Autowired
	private ProductDatabaseService productDatabaseService;
	
	@Autowired
	private  BasketProductRepository basketProductDatabaseHandler;
	
	//private Discount discountValue;
	
	@Autowired(required = false)
	private ShopResearcherService shopResearcher;

	public List<BasketProduct> getAllProducts() {
		return IterableUtils.toList(basketProductDatabaseHandler.findAll());
	}

	public float getTotalPriceForProduct(int id) {
		return basketProductDatabaseHandler.getTotalPriceForProduct(id);
	}
	
	public void removeBasketProduct(int id) {
		basketProductDatabaseHandler.deleteById(id);
	}
	
	public List<BasketProduct> getBasketProductsByShopName(String shopName){
		return basketProductDatabaseHandler.findByShopName(shopName);
	}
	
	public float getTotalPrice() {
		if(this.getAllProducts().isEmpty()) return 0f;
		return basketProductDatabaseHandler.getTotalPrice();
	}

	public float getTotalPrice(String shopName) {
		if ((this.getAllProducts().isEmpty()
				|| this.getBasketProductsByShopName(shopName).isEmpty()) && shopName != null)
			return 0f;
		else if (shopName == null)
			return this.getTotalPrice();
		return basketProductDatabaseHandler.getTotalPriceByShopName(shopName);
	}
	
	public void removeProducts() {
		basketProductDatabaseHandler.deleteAll();
	}
	
	public boolean productAdded(Product product) {
		if (basketProductDatabaseHandler.findByProduct(product) != null)
			return true;
		return false;
	}

	public void addProductToBasket(Integer productId, String shopName) {

		BasketProduct basketProduct;

		if (this.productAdded(productDatabaseService.getProductById(productId))) {
			basketProduct = basketProductDatabaseHandler.findByProductId(productId);
			basketProduct.setQuantity(basketProductDatabaseHandler.findByProductId(productId).getQuantity() + 1);
		} else

		{
			/*basketProduct = new BasketProduct();
			basketProduct.setQuantity(1);
			basketProduct.setShopName(shopName);
			basketProduct.setProduct(productDatabaseService.getProductById(productId));*/
		}

		//basketProductDatabaseHandler.save(basketProduct);
	}

	public int getQuantity(int basketProductId) {
		return basketProductDatabaseService.getQuantity(basketProductId);
	}


	public void changeQuantity(int id, int value) {
		BasketProduct basketProduct = basketProductDatabaseHandler.findById(id);
		System.out.println("PRODUKT : " + basketProduct.getId());
		basketProduct.setQuantity(value);
		basketProductDatabaseHandler.save(basketProduct);
	}
	
	public String formatPrice(float price) {
		return String.format(Locale.US, "%.2f", price);
	}
	
	public String getTotalDiscount(String shop) {
		float discount = (float)(this.getTotalPrice(shop) * ((100.0 - shopResearcher.getDiscount().getValue())/100.0));
		float totalDiscount  = this.getTotalPrice(shop) - discount;
		if(totalDiscount == 0f) return formatPrice(totalDiscount);
		return "-  " + formatPrice(totalDiscount);
	}
	
	public String getFinalPrice(String shop) {
		float price = this.getTotalPrice(shop) - Float.parseFloat(this.getTotalDiscount(shop)); 
		return formatPrice(price);
	}
}
