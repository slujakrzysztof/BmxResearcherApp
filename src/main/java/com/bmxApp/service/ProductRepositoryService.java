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
	
	public ProductDTO getProductByProductNameAndShopName(String productName, String shopName) {
		
		Product product = productRepository.findByProductNameAndShopName(productName, shopName);
		
		return ProductMapper.mapToProductDTO(product);
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

	public Product getProduct(ProductDTO dtoProduct) {
		
		Product product = productRepository.findByProductNameAndShopName(dtoProduct.getProductName(), dtoProduct.getShopName());
		
		return product;
	}
}
