package com.bmxApp.service;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.service.BasketProductDatabaseService;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;

@Service
public class ShoppingCartService {

	@Autowired
	private BasketProductDatabaseService basketProductDatabaseService;

	@Autowired
	private ProductDatabaseService productDatabaseService;

	public List<BasketProduct> getAllProducts() {
		return basketProductDatabaseService.getAllBasketProducts();
	}

	public float getTotalPriceForProduct(int id) {
		return basketProductDatabaseService.getTotalPriceForProduct(id);
	}

	public float getTotalPrice(String shopName) {
		if ((basketProductDatabaseService.getAllBasketProducts().isEmpty()
				|| basketProductDatabaseService.getBasketProductsByShopName(shopName).isEmpty()) && shopName != null)
			return 0f;
		else if (shopName == null)
			return basketProductDatabaseService.getTotalPrice();
		return basketProductDatabaseService.getTotalPriceByShopName(shopName);
	}
	
	public void deleteProducts() {
		basketProductDatabaseService.deleteProducts();
	}

	public void addProductToBasket(Integer productId, String shopName) {

		BasketProduct basketProduct;

		if (basketProductDatabaseService.productAdded(productDatabaseService.getProductById(productId))) {
			basketProduct = basketProductDatabaseService.getProductByProductId(productId);
			basketProduct.setQuantity(basketProductDatabaseService.getProductByProductId(productId).getQuantity() + 1);
		} else

		{
			basketProduct = new BasketProduct();
			basketProduct.setQuantity(1);
			basketProduct.setShopName(shopName);
			basketProduct.setProduct(productDatabaseService.getProductById(productId));
		}

		basketProductDatabaseService.insertOrUpdateBasketProduct(basketProduct);
	}

	public int getQuantity(int basketProductId) {
		return basketProductDatabaseService.getQuantity(basketProductId);
	}

	public void changeQuantity(int id, int value) {
		BasketProduct basketProduct = basketProductDatabaseService.getProductById(id);
		System.out.println("PRODUKT : " + basketProduct.getId());
		basketProduct.setQuantity(value);
		basketProductDatabaseService.insertOrUpdateBasketProduct(basketProduct);
	}

	public List<BasketProduct> getBasketProductsByShopName(String shopName) {
		return basketProductDatabaseService.getBasketProductsByShopName(shopName);
	}

}
