package com.bmxApp.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.Part;
import com.bmxApp.enums.Shop;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;
import com.bmxApp.repository.BasketProductRepository;
import com.bmxApp.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductRepositoryService {

	@Autowired
	ProductRepository productRepository;

	@Transactional
	public ArrayList<ProductDTO> getSearchedProducts(String shopName, String category) {
		
		List<Product> productList;
		ArrayList<ProductDTO> productDTOList = new ArrayList<>();
		
		productList = productRepository.findByShopNameAndCategory(shopName, category);
		productList.forEach(product -> 
			productDTOList.add(ProductMapper.mapToProductDTO(product))
		);
		
		return productDTOList;
	}
	
	public List<Product> findProductsByShopNameAndCategory(String shopName, String category) {
		return productRepository.findByShopNameAndCategory(shopName, category);
	}
	
	public boolean isProductInDatabase(String shopName, String category) {
		List<Product> productList = this.findProductsByShopNameAndCategory(shopName, category);
		if(productList.isEmpty()) return false;
		return true;
	}
	
	public ProductDTO getProductById(int id) {
		
		Product product = productRepository.findById(id);
		
		return ProductMapper.mapToProductDTO(product);
	}

	/*
	@Transactional
	public void insertOrUpdateProduct(Product product) {
		productDatabaseHandler.save(product);
	}

	public Product getProductById(int id) {
		return productDatabaseHandler.findById(id);
	}

	@Transactional
	public void insertAllProducts(Iterable<Product> products) {
		productDatabaseHandler.saveAll(products);
	}

	@Transactional
	public void deleteProduct(Product product) {
		productDatabaseHandler.delete(product);
	}

	@Transactional
	public void deleteAllProducts() {
		productDatabaseHandler.deleteAll();
	}

	@Transactional
	public boolean wasPartSearchedPrevious(String shopName, String category) {
		if (productDatabaseHandler.findByShopNameAndCategory(shopName, category).isEmpty())
			return false;
		return true;
	}

	public boolean wasShopUsed(String shopName) {
		if (productDatabaseHandler.findByShopName(shopName).isEmpty())
			return false;
		return true;
	}

	@Transactional
	public Product getProductByName(String productName, String shopName) {
		return productDatabaseHandler.findByProductNameAndShopName(productName, shopName);
	}
*/
}
