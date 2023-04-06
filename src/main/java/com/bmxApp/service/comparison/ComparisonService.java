package com.bmxApp.service.comparison;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bmxApp.dto.product.CompareProductDTO;
import com.bmxApp.researcher.ShopResearcherService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@Getter
@Setter
//@RequiredArgsConstructor
public class ComparisonService {

	private boolean productOneAdded;
	private boolean productTwoAdded;
	private Map<Integer, CompareProductDTO> compareProducts;
	private final ShopResearcherService shopResearcherService;
	
	
	public ComparisonService(ShopResearcherService shopResearcherService) {
		
		this.shopResearcherService = shopResearcherService;
		productOneAdded = false;
		productTwoAdded = false;
		compareProducts = new HashMap<>();
	}
	
	private CompareProductDTO createCompareProduct(CompareProductDTO product, int index) {
		
		CompareProductDTO compareProduct = product;
		String description = shopResearcherService.getCompareDescription(product.getUri(),index);
		
		compareProduct.setDescription(description);
		
		return compareProduct;
		
	}

	public Map<Integer,CompareProductDTO> compare(CompareProductDTO product) {
		
		System.out.println("1 ADDED: " + productOneAdded);
		System.out.println("2 ADDED: " + productTwoAdded);
		
		if(productOneAdded && !productTwoAdded) {
			
			compareProducts.put(2, this.createCompareProduct(product, 2));
			setProductTwoAdded(true);
		}
		else if (!productOneAdded && !productTwoAdded) {
			
			compareProducts.put(1, this.createCompareProduct(product, 1));
			compareProducts.put(2, CompareProductDTO.builder()
						.productName("")
						.shopName("")
						.price(new BigDecimal(0))
						.description("")
						.build());
			
			setProductOneAdded(true);
		}
		else if(productOneAdded && productTwoAdded) {
			
			setProductOneAdded(false);
			compare(product);
		}
		else if(!productOneAdded && productTwoAdded) {
			
			compareProducts.put(1, this.createCompareProduct(product, 1));
			setProductOneAdded(true);
			setProductTwoAdded(false);
		}
		
		
		return compareProducts;
	}
}
