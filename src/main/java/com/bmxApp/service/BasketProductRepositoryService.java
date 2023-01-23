package com.bmxApp.service;

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

@Service
public class BasketProductRepositoryService {

	@Autowired
	BasketProductRepository basketProductRepository;

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
	
	/*public LinkedList<BasketProductDTO> getBasketProductsDTOByShopName(String shopName) {
		
		List<BasketProduct> basketProducts = basketProductRepository.findByShopName(shopName);
		LinkedList<BasketProductDTO> dtoBasketProducts = new LinkedList<>();
		
		basketProducts.forEach(basketProduct -> dtoBasketProducts.add(BasketProductMapper.mapToBasketProductDTO(basketProduct)));
		
		return dtoBasketProducts;
	}*/
	
	public ArrayList<BasketProduct> getBasketProductsByShopName(String shopName) {
		return (ArrayList<BasketProduct>) basketProductRepository.findByShopName(shopName);
	}

	/*public LinkedList<BasketProductDTO> getBasketProductsDTO() {
		
		List<BasketProduct> basketProductList = basketProductRepository.findAll();
		LinkedList<BasketProductDTO> basketProductDTOList = new LinkedList<>();
		
		basketProductList.forEach(basketProduct -> basketProductDTOList.add(BasketProductMapper.mapToBasketProductDTO(basketProduct)));
		
		return basketProductDTOList;
	}*/

	public boolean isProductInDatabase(Product product) {
		
		Optional<BasketProduct> basketProduct = Optional.ofNullable(this.getBasketProductByProduct(product));
		
		System.out.println("DBAAAAAAAAAAAAAASKET : " + basketProduct);
		
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

	/*public BasketProductDTO getBasketProductByProductId(int productId) {
		
		Optional<BasketProduct> basketProduct = Optional.ofNullable(basketProductRepository.findByProductId(productId));
		
		if(basketProduct.isEmpty()) return null;
		return BasketProductMapper.mapToBasketProductDTO(basketProduct.get());
	}*/

	/*public BasketProductDTO getBasketProductById(int id) {
		
		BasketProduct basketProduct = basketProductRepository.findById(id);
		
		return BasketProductMapper.mapToBasketProductDTO(basketProduct);
	}*/

	public void insertUpdateBasketProduct(BasketProduct basketProduct) {
		
		//BasketProduct basketProduct = BasketProductMapper.mapToBasketProduct(basketProduct);
		
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
	
}