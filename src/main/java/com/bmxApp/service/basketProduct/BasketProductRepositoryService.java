package com.bmxApp.service.basketProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.mapper.basketProduct.BasketProductMapper;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.basketProduct.BasketProduct;
import com.bmxApp.model.product.Product;
import com.bmxApp.repository.BasketProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasketProductRepositoryService {

	private final BasketProductRepository basketProductRepository;

	public float getTotalPrice() {
		if(this.getBasketProducts().isEmpty()) return 0f;
		return basketProductRepository.getTotalPrice();
	}
	
	public ArrayList<BasketProduct> getBasketProducts() {
		return (ArrayList<BasketProduct>) basketProductRepository.findAll();
	}
	
	public float getTotalPriceForShop(String shopName) {
		
		return basketProductRepository.getTotalPriceForShop(shopName);
	}

	public float getTotalPriceForBasketProduct(int id) {
		
		return basketProductRepository.getTotalPriceForBasketProduct(id);
	}
	
	public ArrayList<BasketProduct> getBasketProductsByShopName(String shopName) {
		return (ArrayList<BasketProduct>) basketProductRepository.findByShopName(shopName);
	}

	public boolean isProductInDatabase(Product product) {
		
		Optional<BasketProduct> basketProduct = Optional.ofNullable(this.getBasketProductByProduct(product));
				
		if (basketProduct.isPresent())
			return true;
		return false;
	}
	
	public BasketProduct getBasketProductById(int id) {
		
		Optional<BasketProduct> basketProduct = Optional.ofNullable(basketProductRepository.findById(id));
		
		if(basketProduct.isEmpty()) return null;
		return basketProduct.get();
	}
	
	public BasketProduct getBasketProductByProduct(Product product) {
		
		Optional<BasketProduct> basketProduct = Optional.ofNullable(basketProductRepository.findByProduct(product));
		
		if(basketProduct.isEmpty()) return null;
		return basketProduct.get();
	}
	
	public Map<Integer, Float> getTotalPriceForEachBasketProduct() {
		
		ArrayList<BasketProduct> basketProducts = this.getBasketProducts();
		Map<Integer, Float> basketProductsPrices = new HashMap<>();
		
		basketProducts.forEach(basketProduct -> {
			
			int productId = basketProduct.getProduct().getId();
			float totalPrice = basketProductRepository.getTotalPriceForBasketProduct(productId);
			basketProductsPrices.put(productId, totalPrice);
		});
		
		return basketProductsPrices;
	}

	public void insertUpdateBasketProduct(BasketProduct basketProduct) {
				
		basketProductRepository.save(basketProduct);
	}

	public int getQuantity(int productId) {
		return (int) basketProductRepository.getProductQuantity(productId);
	}
	
	public void deleteBasketProducts() {
		basketProductRepository.deleteAll();
	}
	
	public void deleteBasketProductById(int id) {
		basketProductRepository.deleteById(id);
	}
	
	public void deleteBasketProductByProduct(Product product) {
		basketProductRepository.deleteByProduct(product);
	}
	
	public ArrayList<BasketProductDTO> getBasketProductsDTO() {
		
		ArrayList<BasketProduct> basketProductsList = this.getBasketProducts(); 
		ArrayList<BasketProductDTO> basketProductsDtoList = new ArrayList<>();
		
		basketProductsList.forEach(basketProduct -> {
			BasketProductDTO dtoBasketProduct = BasketProductMapper.mapToBasketProductDTO(basketProduct);
			basketProductsDtoList.add(dtoBasketProduct);
		});
		
		return basketProductsDtoList;
	}
	
}