package com.bmxApp.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.mapper.basketProduct.BasketProductMapper;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;
import com.bmxApp.repository.BasketProductRepository;

@Service
public class BasketProductRepositoryService {

	@Autowired
	BasketProductRepository basketProductRepository;

	public float getTotalPrice() {
		if(this.getBasketProducts().isEmpty()) return 0f;
		return basketProductRepository.getTotalPrice();
	}
	
	public float getTotalPriceForShop(String shopName) {
		
		return basketProductRepository.getTotalPriceForShop(shopName);
	}

	public float getTotalPriceForBasketProduct(int id) {
		
		return basketProductRepository.getTotalPriceForBasketProduct(id);
	}
	
	public LinkedList<BasketProductDTO> getBasketProductsByShopName(String shopName) {
		
		List<BasketProduct> basketProducts = basketProductRepository.findByShopName(shopName);
		LinkedList<BasketProductDTO> dtoBasketProducts = new LinkedList<>();
		
		basketProducts.forEach(basketProduct -> dtoBasketProducts.add(BasketProductMapper.mapToBasketProductDTO(basketProduct)));
		
		return dtoBasketProducts;
	}

	public BasketProductDTO getBasketProductByProduct(ProductDTO productDTO) {
		
		Product product = ProductMapper.mapToProduct(productDTO);
		BasketProduct basketProduct = basketProductRepository.findByProduct(product);
		
		return BasketProductMapper.mapToBasketProductDTO(basketProduct);
	}

	public ArrayList<BasketProductDTO> getBasketProducts() {
		
		List<BasketProduct> basketProductList = basketProductRepository.findAll();
		ArrayList<BasketProductDTO> basketProductDTOList = new ArrayList<>();
		
		basketProductList.forEach(basketProduct -> basketProductDTOList.add(BasketProductMapper.mapToBasketProductDTO(basketProduct)));
		
		return basketProductDTOList;
	}

	public boolean isProductInDatabase(ProductDTO productDTO) {
		
		Optional<BasketProductDTO> dtoBasketProduct = Optional.ofNullable(this.getBasketProductByProduct(productDTO));
		
		if (dtoBasketProduct.isPresent())
			return true;
		return false;
	}

	public BasketProductDTO getBasketProductByProductId(int productId) {
		
		BasketProduct basketProduct = basketProductRepository.findByProductId(productId);
		
		return BasketProductMapper.mapToBasketProductDTO(basketProduct);
	}

	public BasketProductDTO getBasketProductById(int id) {
		
		BasketProduct basketProduct = basketProductRepository.findById(id);
		
		return BasketProductMapper.mapToBasketProductDTO(basketProduct);
	}

	public void insertUpdateBasketProduct(BasketProductDTO basketProductDTO) {
		
		BasketProduct basketProduct = BasketProductMapper.mapToBasketProduct(basketProductDTO);
		
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
	

}