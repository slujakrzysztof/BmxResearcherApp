package com.bmxApp.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.mapper.basketProduct.BasketProductMapper;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;
import com.bmxApp.researcher.ShopResearcherService;

@Service
public class ShoppingCartService {

	@Autowired
	private BasketProductRepositoryService basketProductRepositoryService;

	@Autowired
	private ProductRepositoryService productRepositoryService;

	@Autowired(required = false)
	private ShopResearcherService shopResearcher;

	public ArrayList<BasketProduct> getBasketProducts() {

		return basketProductRepositoryService.getBasketProducts();
	}

	public LinkedList<BasketProductDTO> getBasketProductsInCart(String shopName) {
		
		ArrayList<BasketProduct> basketProducts = this.getBasketProducts();
		LinkedList<BasketProductDTO> dtoBasketProducts = new LinkedList<>();
		
		basketProducts.forEach(basketProduct -> {
			BasketProductDTO dtoBasketProduct = BasketProductMapper.mapToBasketProductDTO(basketProduct);
			dtoBasketProducts.add(dtoBasketProduct);
		});
		
		if(Optional.ofNullable(shopName).isPresent())  {	
			
			List<BasketProductDTO> dtoBasketProductsByShopName = dtoBasketProducts.stream().
					filter(basketProduct -> basketProduct.getShopName().equals(shopName)).collect(Collectors.toList());
			
			return (LinkedList<BasketProductDTO>) dtoBasketProductsByShopName;
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

	public LinkedList<BasketProduct> getBasketProductsByShopName(String shopName) {

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
		
		/*BasketProductDTO dtoBasketProduct;

		ProductDTO dtoProduct = productRepositoryService.getProductByProductNameAndShopName(productName, shopName);

		Product product = productRepositoryService.getProduct(dtoProduct);

		if (this.isProductInCart(productId)) {
			dtoBasketProduct = basketProductRepositoryService.getBasketProductByProductId(productId);
			dtoBasketProduct.setQuantity(dtoBasketProduct.getQuantity() + 1);
		} else {
			System.out.println("I'm here");
			dtoBasketProduct = BasketProductDTO.builder().productDTO(dtoProduct).quantity(1).shopName(shopName).build();
		}

		basketProductRepositoryService.insertUpdateBasketProduct(dtoBasketProduct, product);*/
	}

	public int getQuantity(int basketProductId) {
		return basketProductRepositoryService.getQuantity(basketProductId);
	}

	public void changeQuantity(BasketProduct basketProduct, int value) {

		BasketProduct bProduct = basketProduct;
		
		bProduct.setQuantity(basketProduct.getQuantity() + value);
		
		if(bProduct.getQuantity() <= 0) {
			this.deleteBasketProductById(bProduct.getId());
			return;
		}
		
		basketProductRepositoryService.insertUpdateBasketProduct(bProduct);
		
		/*BasketProductDTO dtoBasketProduct = BasketProductMapper.mapToBasketProductDTO(basketProduct);

		Product product = basketProduct.getProduct();

		int productQuantity = dtoBasketProduct.getQuantity() + value;

		if (productQuantity <= 0) {
			this.deleteBasketProductById(basketProduct.getId());
			return;
		}

		dtoBasketProduct.setQuantity(productQuantity);
		basketProductRepositoryService.insertUpdateBasketProduct(dtoBasketProduct, product);*/
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
