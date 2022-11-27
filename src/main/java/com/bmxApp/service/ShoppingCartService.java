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

	public float getTotalPrice() {
		if (basketProductDatabaseService.getAllBasketProducts().isEmpty())
			return 0f;
		return basketProductDatabaseService.getTotalPrice();
	}

	public void addProductToBasket(Integer productId, Integer quantity) {

		BasketProduct basketProduct;

		if (basketProductDatabaseService.productAdded(productDatabaseService.getProductById(productId))) {
			basketProduct = basketProductDatabaseService.getProductByProductId(productId);
			basketProduct.setQuantity(
					basketProductDatabaseService.getProductByProductId(productId).getQuantity() + quantity);
		} else

		{
			basketProduct = new BasketProduct();
			basketProduct.setQuantity(quantity);
			basketProduct.setProduct(productDatabaseService.getProductById(productId));
		}

		basketProductDatabaseService.insertOrUpdateBasketProduct(basketProduct);

	}

}
