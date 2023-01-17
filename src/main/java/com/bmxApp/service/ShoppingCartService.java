package com.bmxApp.service;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.mapper.basketProduct.BasketProductMapper;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.researcher.ShopResearcherService;

@Service
public class ShoppingCartService {

	@Autowired
	private BasketProductRepositoryService basketProductRepositoryService;

	@Autowired
	private ProductRepositoryService productRepositoryService;

	@Autowired(required = false)
	private ShopResearcherService shopResearcher;

	public LinkedList<BasketProductDTO> getBasketProducts() {

		return basketProductRepositoryService.getBasketProducts();
	}
	
	public LinkedList<BasketProductDTO> getBasketProductsInCart(String shopName) {
		
		if(Optional.ofNullable(shopName).isPresent()) return this.getBasketProductsByShopName(shopName);
		return this.getBasketProducts();

	}

	public float getTotalPriceForBasketProduct(int id) {

		return basketProductRepositoryService.getTotalPriceForBasketProduct(id);
	}

	public void deleteBasketProductById(int id) {

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

	public void addProductToCart(String productName, String shopName) {

		BasketProductDTO dtoBasketProduct;

		ProductDTO dtoProduct = productRepositoryService.getProductByProductNameAndShopName(productName, shopName);

		if (this.isProductInDatabase(dtoProduct)) {
			dtoBasketProduct = basketProductRepositoryService.getBasketProductByProduct(dtoProduct);
			dtoBasketProduct.setQuantity(dtoBasketProduct.getQuantity() + 1);
		} else {
			dtoBasketProduct = BasketProductDTO.builder()
											   .productDTO(dtoProduct)
											   .quantity(1)
											   .shopName(shopName)
											   .build();
		}

		basketProductRepositoryService.insertUpdateBasketProduct(dtoBasketProduct);
	}

	public int getQuantity(int basketProductId) {
		return basketProductRepositoryService.getQuantity(basketProductId);
	}

	public void changeQuantity(BasketProduct basketProduct, int value) {
		
		BasketProductDTO dtoBasketProduct = BasketProductMapper.mapToBasketProductDTO(basketProduct);
		int productQuantity = dtoBasketProduct.getQuantity() + value;
		
		if(productQuantity <= 0) {
			this.deleteBasketProductById(basketProduct.getId());
			return;
		}
		
		dtoBasketProduct.setQuantity(productQuantity);
		basketProductRepositoryService.insertUpdateBasketProduct(dtoBasketProduct);
	}

	public String formatPrice(double price) {
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
