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
@RequiredArgsConstructor
public class ComparisonControllerService {

	private boolean productOneAdded;
	private boolean productTwoAdded;
	private final ShopResearcherService shopResearcher;
	
	
	private CompareProductDTO createCompareProduct(CompareProductDTO product) {
		
		CompareProductDTO compareProduct = product;
		String description = shopResearcher.getDescriptionFromPage(product.getUri());
		
		compareProduct.setDescription(description);
		
		return compareProduct;
		
	}

	public Map<Integer,CompareProductDTO> compare(CompareProductDTO product) {

		Map<Integer, CompareProductDTO> compareProducts = new HashMap<>();
		
		if(productOneAdded && !productTwoAdded) {
			
			compareProducts.put(2, this.createCompareProduct(product));
			setProductTwoAdded(true);
		}
		else if (!productOneAdded && !productTwoAdded) {
			
			compareProducts.put(1, this.createCompareProduct(product));
			compareProducts.put(2, CompareProductDTO.builder()
						.productName("")
						.shopName("")
						.price(new BigDecimal(0))
						.description("")
						.build());
			
			setProductOneAdded(true);
		}
		
		
		return compareProducts;
	}
}
