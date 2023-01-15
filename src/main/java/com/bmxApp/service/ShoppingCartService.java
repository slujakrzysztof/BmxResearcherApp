package com.bmxApp.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.builder.basketProduct.BasketProductBuilder;
import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;
import com.bmxApp.repository.BasketProductRepository;
import com.bmxApp.researcher.ShopResearcherService;

@Service
public class ShoppingCartService {

	@Autowired
	private BasketProductRepositoryService basketProductRepositoryService;

	@Autowired
	private ProductRepositoryService productRepositoryService;

	@Autowired(required = false)
	private ShopResearcherService shopResearcher;

	public ArrayList<BasketProductDTO> getBasketProducts() {

		return basketProductRepositoryService.getBasketProducts();
	}

	public float getTotalPriceForProduct(int id) {

		return basketProductRepositoryService.getTotalPriceForBasketProduct(id);
	}

	public void deleteBasketProduct(int id) {

		basketProductRepositoryService.deleteBasketProductById(id);
	}

	public LinkedList<BasketProductDTO> getBasketProductsByShopName(String shopName) {

		return basketProductRepositoryService.getBasketProductsByShopName(shopName);
	}

	public float getTotalPrice() {

		return basketProductRepositoryService.getTotalPrice();
	}

	public float getTotalPriceForShop(String shopName) {
		if ((this.getBasketProducts().isEmpty() || this.getBasketProductsByShopName(shopName).isEmpty())
				&& shopName != null)
			return 0f;
		else if (shopName == null)
			return this.getTotalPrice();
		return basketProductRepositoryService.getTotalPriceForShop(shopName);
	}

	public void deleteBasketProducts() {
		basketProductRepositoryService.deleteBasketProducts();
	}

	public boolean isProductInDatabase(ProductDTO productDTO) {
		return basketProductRepositoryService.isProductInDatabase(productDTO);
	}

	public void addProductToCart(Integer productId, String shopName) {

		BasketProductDTO dtoBasketProduct;

		ProductDTO dtoProduct = productRepositoryService.getProductById(productId);

		if (this.isProductInDatabase(dtoProduct)) {
			dtoBasketProduct = basketProductRepositoryService.getBasketProductByProduct(dtoProduct);
			dtoBasketProduct.setQuantity(dtoBasketProduct.getQuantity() + 1);
		} else {
			dtoBasketProduct = BasketProductBuilder.buildBasketProduct(dtoProduct, 1, shopName);
		}

		basketProductRepositoryService.insertUpdateBasketProduct(dtoBasketProduct);
	}

	public int getQuantity(int basketProductId) {
		return basketProductRepositoryService.getQuantity(basketProductId);
	}

	public void changeQuantity(int id, int value) {
		BasketProductDTO dtoBasketProduct = basketProductRepositoryService.getBasketProductById(id);
		dtoBasketProduct.setQuantity(value);
		basketProductRepositoryService.insertUpdateBasketProduct(dtoBasketProduct);
	}

	public String formatPrice(float price) {
		return String.format(Locale.US, "%.2f", price);
	}

	public String getTotalDiscount(String shopName) {
		float discount = (float) (this.getTotalPriceForShop(shopName)
				* ((100.0 - shopResearcher.getDiscount().getValue()) / 100.0));
		float totalDiscount = this.getTotalPriceForShop(shopName) - discount;
		if (totalDiscount == 0f)
			return formatPrice(totalDiscount);
		return "-  " + formatPrice(totalDiscount);
	}

	public String getFinalPrice(String shopName) {
		float price = this.getTotalPriceForShop(shopName) - Float.parseFloat(this.getTotalDiscount(shopName));
		return formatPrice(price);
	}
}
