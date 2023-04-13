package com.bmxApp.service.cart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.formatter.ProductFormatter;
import com.bmxApp.mapper.basketProduct.BasketProductDTOMapper;
import com.bmxApp.model.basketProduct.BasketProduct;
import com.bmxApp.model.product.Product;
import com.bmxApp.service.database.BasketProductRepositoryService;
import com.bmxApp.service.database.ProductRepositoryService;
import com.bmxApp.service.discount.DiscountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

	private final BasketProductRepositoryService basketProductRepositoryService;
	private final ProductRepositoryService productRepositoryService;
	private final DiscountService discountService;
	private final BasketProductDTOMapper basketProductDTOMapper;

	public List<BasketProductDTO> getBasketProducts(String shopName) {

		List<BasketProduct> basketProducts;
		List<BasketProductDTO> basketProductsDTO;

		if (Optional.ofNullable(shopName).isPresent())
			basketProductsDTO = this.getBasketProductsByShopName(shopName);

		else {

			basketProducts = basketProductRepositoryService.getBasketProducts();
			basketProductsDTO = basketProducts.stream().map(product -> basketProductDTOMapper.apply(product))
					.collect(Collectors.toList());
		}
		
		return basketProductsDTO;
	}

	public List<BasketProductDTO> getBasketProducts(String shopName, String discountValue) {

		List<BasketProductDTO> basketProductsDTO = getBasketProducts(shopName);

		if (discountValue != null)
			basketProductsDTO = discountService.getBasketProductsWithDiscount(basketProductsDTO);

		return basketProductsDTO;
	}

	public void setCartDiscount(int value) {

		discountService.setDiscount(value);
	}

	public void deleteBasketProductByProductId(int productId) {

		Product product = productRepositoryService.getProductById(productId);
		int id = basketProductRepositoryService.getBasketProductByProduct(product).getId();

		basketProductRepositoryService.deleteBasketProductById(id);
	}

	public BigDecimal getTotalPriceForBasketProduct(int id) {

		return basketProductRepositoryService.getTotalPriceForBasketProduct(id);
	}

	public void deleteBasketProductByProduct(Product product) {

		basketProductRepositoryService.deleteBasketProductByProduct(product);
	}

	public void deleteBasketProductById(int id) {

		basketProductRepositoryService.deleteBasketProductById(id);
	}

	public List<BasketProductDTO> getBasketProductsByShopName(String shopName) {

		List<BasketProduct> basketProducts = basketProductRepositoryService.getBasketProductsByShopName(shopName);
		List<BasketProductDTO> basketProductsDTO;

		basketProductsDTO = basketProducts.stream().map(product -> basketProductDTOMapper.apply(product))
				.collect(Collectors.toList());

		return basketProductsDTO;
	}

	public BigDecimal getTotalPrice() {

		return basketProductRepositoryService.getTotalPrice();
	}

	public BigDecimal getTotalPriceForShop(String shopName) {

		Optional<String> shop = Optional.ofNullable(shopName);

		if (shop.isPresent()) {
			if (this.getBasketProducts(shopName).isEmpty())
				return new BigDecimal(0);
			else
				return basketProductRepositoryService.getTotalPriceForShop(shopName);
		}

		return this.getTotalPrice();
	}

	public Map<Integer, BigDecimal> getTotalPriceForEachBasketProduct(String discountValue) {

		Optional<String> discountOptional = Optional.ofNullable(discountValue);
		int discount = 0;
		if (discountOptional.isPresent())
			discount = Integer.parseInt(discountValue);
		return basketProductRepositoryService.getTotalPriceForEachBasketProduct(discount);
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

	public BigDecimal getTotalDiscount(String shopName) {

		BigDecimal discount = ProductFormatter.format(this.getTotalPriceForShop(shopName).multiply(getDiscountValue()));
		BigDecimal totalDiscount = this.getTotalPriceForShop(shopName).subtract(discount).multiply(new BigDecimal(-1));
		return totalDiscount;
	}

	public BigDecimal getDiscountValue() {

		BigDecimal discount = new BigDecimal((100.0 - discountService.getDiscount()) / 100.0);

		return discount;
	}

	public BigDecimal getFinalPrice(String shopName) {

		BigDecimal finalPrice = ProductFormatter
				.format(this.getTotalPriceForShop(shopName).add(this.getTotalDiscount(shopName)));
		return finalPrice;
	}

}
