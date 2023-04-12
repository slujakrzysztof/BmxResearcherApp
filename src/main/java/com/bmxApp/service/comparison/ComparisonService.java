package com.bmxApp.service.comparison;

import java.math.BigDecimal;
import java.net.http.HttpRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.EnumerationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

import com.bmxApp.dto.product.CompareProductDTO;
import com.bmxApp.researcher.ShopResearcherService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@Getter
@Setter
public class ComparisonService {

	private final CompareProductDTO NULL_PRODUCT;
	private final String HEADER;

	private boolean productOneAdded;
	private boolean productTwoAdded;
	private Map<Integer, CompareProductDTO> compareProducts;
	private final ShopResearcherService shopResearcherService;
	private String currentUrl;

	public ComparisonService(ShopResearcherService shopResearcherService) {

		this.shopResearcherService = shopResearcherService;
		NULL_PRODUCT = CompareProductDTO.builder().productName("").shopName("").price(new BigDecimal(0)).description("")
				.uri("").imageUrl(null).build();
		HEADER = "productDeleted";
		productOneAdded = false;
		productTwoAdded = false;
		compareProducts = new HashMap<>();
	}

	private CompareProductDTO createCompareProduct(CompareProductDTO product) {

		CompareProductDTO compareProduct = product;
		String description = shopResearcherService.getCompareDescription(product.getUri());

		compareProduct.setDescription(description);

		return compareProduct;

	}

	public void compare(CompareProductDTO product) {

		if (product.getProductName() != null) {
			
			if (productOneAdded && !productTwoAdded) {

				compareProducts.put(2, this.createCompareProduct(product));
				setProductTwoAdded(true);
			} else if (!productOneAdded && !productTwoAdded) {

				compareProducts.put(1, this.createCompareProduct(product));
				compareProducts.put(2, NULL_PRODUCT);

				setProductOneAdded(true);
			} else if (productOneAdded && productTwoAdded) {

				setProductOneAdded(false);
				compare(product);
			} else if (!productOneAdded && productTwoAdded) {

				compareProducts.put(1, this.createCompareProduct(product));
				setProductOneAdded(true);
				setProductTwoAdded(false);
			}
		}
	}
	
	public boolean isComparatorFull() {
		
		boolean full = ((productOneAdded && productTwoAdded) ? true : false);
		
		return !full;
		
	}

	public void deleteProduct(int id) {

		compareProducts.put(id, NULL_PRODUCT);

		switch (id) {

		case 1: {
			setProductOneAdded(false);
			break;
		}
		case 2:
			setProductTwoAdded(false);
		}
	}

}
