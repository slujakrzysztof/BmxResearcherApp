package com.bmxApp.service.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.mapper.basketProduct.BasketProductMapper;
import com.bmxApp.model.basketProduct.BasketProduct;
import com.bmxApp.model.product.Product;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.basketProduct.BasketProductRepositoryService;
import com.bmxApp.service.product.ProductRepositoryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

	private final BasketProductRepositoryService basketProductRepositoryService;
	private final ProductRepositoryService productRepositoryService;
	private final ShopResearcherService shopResearcher;

	public ArrayList<BasketProduct> getBasketProducts() {

		return basketProductRepositoryService.getBasketProducts();
	}

	public void setCartDiscount(int value) {

		shopResearcher.getDiscount().setValue(value);
	}

	public List<BasketProductDTO> getBasketProductsInCart(String shopName) {

		List<BasketProduct> basketProducts = this.getBasketProducts();
		List<BasketProductDTO> dtoBasketProducts = new ArrayList<>();

		basketProducts.forEach(basketProduct -> {
			BasketProductDTO dtoBasketProduct = BasketProductMapper.mapToBasketProductDTO(basketProduct);
			dtoBasketProducts.add(dtoBasketProduct);
		});

		if (Optional.ofNullable(shopName).isPresent()) {

			List<BasketProductDTO> dtoBasketProductsByShopName = dtoBasketProducts.stream()
					.filter(basketProduct -> basketProduct.getShopName().equalsIgnoreCase(shopName))
					.collect(Collectors.toList());

			return (ArrayList<BasketProductDTO>) dtoBasketProductsByShopName;
		}
		return dtoBasketProducts;
	}

	public void deleteBasketProductByProductId(int productId) {

		Product product = productRepositoryService.getProductById(productId);
		int id = basketProductRepositoryService.getBasketProductByProduct(product).getId();

		basketProductRepositoryService.deleteBasketProductById(id);
	}

	public float getTotalPriceForBasketProduct(int id) {

		return basketProductRepositoryService.getTotalPriceForBasketProduct(id);
	}

	public void deleteBasketProductByProduct(Product product) {

		basketProductRepositoryService.deleteBasketProductByProduct(product);
	}

	public void deleteBasketProductById(int id) {

		basketProductRepositoryService.deleteBasketProductById(id);
	}

	public ArrayList<BasketProduct> getBasketProductsByShopName(String shopName) {

		return basketProductRepositoryService.getBasketProductsByShopName(shopName);
	}

	public float getTotalPrice() {

		return basketProductRepositoryService.getTotalPrice();
	}

	public float getTotalPriceForShop(String shopName) {

		Optional<String> shop = Optional.ofNullable(shopName);

		if (shop.isPresent()) {
			if (this.getBasketProducts().isEmpty() || this.getBasketProductsByShopName(shopName).isEmpty())
				return 0f;
			else
				return basketProductRepositoryService.getTotalPriceForShop(shopName);
		}

		return this.getTotalPrice();
	}

	public Map<Integer, Float> getTotalPriceForEachBasketProduct() {

		return basketProductRepositoryService.getTotalPriceForEachBasketProduct();
	}

	public void deleteBasketProducts() {
		basketProductRepositoryService.deleteBasketProducts();
	}

	public boolean isProductInCart(Product product) {

		return basketProductRepositoryService.isProductInDatabase(product);
	}

	public void addProductToCart(String productName, String shopName) {

		Product product = productRepositoryService.getProductByProductNameAndShopName(productName, shopName);
		BasketProduct basketProduct;

		if (this.isProductInCart(product)) {
			basketProduct = basketProductRepositoryService.getBasketProductByProduct(product);
			basketProduct.setQuantity(basketProduct.getQuantity() + 1);
		} else {
			basketProduct = new BasketProduct(product, 1, shopName);
		}

		basketProductRepositoryService.insertUpdateBasketProduct(basketProduct);
	}

	public int getQuantity(int basketProductId) {
		return basketProductRepositoryService.getQuantity(basketProductId);
	}

	public void changeQuantity(int productId, String quantityContainer, String quantityValue) {

		Product product = productRepositoryService.getProductById(productId);
		BasketProduct basketProduct = basketProductRepositoryService.getBasketProductByProduct(product);

		int actualQuantity = basketProduct.getQuantity();
		Optional<String> changeInContainer = Optional.ofNullable(quantityContainer);

		changeInContainer.ifPresentOrElse(change -> basketProduct.setQuantity(Integer.parseInt(change)),
				() -> basketProduct.setQuantity(actualQuantity + Integer.parseInt(quantityValue)));

		if (basketProduct.getQuantity() <= 0) {
			this.deleteBasketProductById(basketProduct.getId());
			return;
		}

		basketProductRepositoryService.insertUpdateBasketProduct(basketProduct);
	}

	public String formatPrice(double price) {
		return String.format(Locale.US, "%.2f", price);
	}

	public String getTotalDiscount(String shopName) {

		int discountValue = shopResearcher.getDiscount().getValue();
		float discount = (float) (this.getTotalPriceForShop(shopName) * ((100.0 - discountValue) / 100.0));
		float totalDiscount = (this.getTotalPriceForShop(shopName) - discount) * (-1);
		return formatPrice(totalDiscount);
	}

	public int getDiscountValue() {

		return this.shopResearcher.getDiscount().getValue();
	}

	public String getFinalPrice(String shopName) {

		float price = this.getTotalPriceForShop(shopName) + Float.parseFloat(this.getTotalDiscount(shopName));
		return formatPrice(price);
	}

	public String getCartURL(HttpServletRequest request) {

		return request.getRequestURL().toString();
	}

}
