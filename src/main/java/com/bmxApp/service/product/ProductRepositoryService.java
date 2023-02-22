package com.bmxApp.service.product;

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
import com.bmxApp.model.basketProduct.BasketProduct;
import com.bmxApp.model.product.Product;
import com.bmxApp.repository.BasketProductRepository;
import com.bmxApp.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductRepositoryService {

	private final ProductRepository productRepository;

	@Transactional
	public List<Product> getSearchedProducts(String shopName, String category) {

		if (shopName.equalsIgnoreCase(Shop.ALLSHOPS.name())) {

			List<Product> productList = new ArrayList<>();
			for (Shop shop : Shop.getShops()) 
				productList.addAll(productRepository.findByShopNameAndCategory(shop.name(), category));
			return productList;
		}

		return productRepository.findByShopNameAndCategory(shopName, category);
	}

	public Product getProductByProductNameAndShopName(String productName, String shopName) {

		Product product = productRepository.findByProductNameAndShopName(productName, shopName);

		return product;
	}
	
	public List<Product> getRequestedItem(String value) {
		
		return productRepository.findRequestedItems(value);
	}

	public List<Product> getProductsByShopNameAndCategory(String shopName, String category) {

		return productRepository.findByShopNameAndCategory(shopName, category);
	}

	public boolean areCategoryAndShopNameInDatabase(String shopName, String category) {

		List<Product> productList = this.getProductsByShopNameAndCategory(shopName, category);
		if (productList.isEmpty())
			return false;
		return true;
	}

	public Product getProductById(int id) {

		Product product = productRepository.findById(id);

		return product;
	}

	public boolean isProductInDatabase(ProductDTO dtoProduct) {

		String productName = dtoProduct.getProductName();
		String shopName = dtoProduct.getShopName();
		Optional<Product> product = Optional.ofNullable(this.getProductByProductNameAndShopName(productName, shopName));

		if (product.isPresent() && dtoProduct.equals(ProductMapper.mapToProductDTO(product.get())))
			return true;
		return false;
	}

	public void insertUpdateProduct(ProductDTO dtoProduct) {

		Product product = productRepository.findByProductNameAndShopName(dtoProduct.getProductName(),
				dtoProduct.getShopName());

		if (product == null)
			product = ProductMapper.mapToProduct(dtoProduct);

		productRepository.save(product);
	}
}
