package com.bmxApp.service.database;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.mapper.basketProduct.BasketProductDTOMapper;
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
	private final BasketProductDTOMapper basketProductDTOMapper;

	public BigDecimal getTotalPrice() {
		
		BigDecimal totalPrice = new BigDecimal(basketProductRepository.getTotalPrice());
		
		if (this.getBasketProducts().isEmpty())
			return new BigDecimal(0);
		
		return totalPrice.setScale(2, RoundingMode.HALF_UP);
	}

	public ArrayList<BasketProduct> getBasketProducts() {
		return (ArrayList<BasketProduct>) basketProductRepository.findAll();
	}

	public BigDecimal getTotalPriceForShop(String shopName) {

		BigDecimal price = new BigDecimal(basketProductRepository.getTotalPriceForShop(shopName));
		
		return price.setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getTotalPriceForBasketProduct(int id) {

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

		if (basketProduct.isEmpty())
			return null;
		return basketProduct.get();
	}

	public BasketProduct getBasketProductByProduct(Product product) {

		Optional<BasketProduct> basketProduct = Optional.ofNullable(basketProductRepository.findByProduct(product));

		if (basketProduct.isEmpty())
			return null;
		return basketProduct.get();
	}

	public Map<Integer, BigDecimal> getTotalPriceForEachBasketProduct(int discountValue) {

		List<BasketProduct> basketProducts = this.getBasketProducts();
		Map<Integer, BigDecimal> basketProductsPrices = new HashMap<>();

		BigDecimal discount = new BigDecimal((100.0 - discountValue)/100.0);
		
		basketProducts.forEach(basketProduct -> {

			int productId = basketProduct.getProduct().getId();
	
			BigDecimal totalPrice = basketProductRepository.getTotalPriceForBasketProduct(productId).multiply(discount).setScale(2, RoundingMode.HALF_UP);
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

	public List<BasketProductDTO> getBasketProductsDTO() {

		List<BasketProduct> basketProductsList = this.getBasketProducts();
		List<BasketProductDTO> basketProductsDtoList = new ArrayList<>();

		basketProductsDtoList = basketProductsList.stream()
				.map(basketProduct -> basketProductDTOMapper.apply(basketProduct)).collect(Collectors.toList());

		/*
		 * basketProductsList.forEach(basketProduct -> { BasketProductDTO
		 * dtoBasketProduct = BasketProductMapper.mapToBasketProductDTO(basketProduct);
		 * basketProductsDtoList.add(dtoBasketProduct); });
		 */

		return basketProductsDtoList;
	}

}