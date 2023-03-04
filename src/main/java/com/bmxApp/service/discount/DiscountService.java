package com.bmxApp.service.discount;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.formatter.product.ProductFormatter;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.model.product.Product;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.product.ProductRepositoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiscountService {

	private final ShopResearcherService shopResearcherService;
	private final ProductRepositoryService productRepositoryService;
	private final ProductDTOMapper productDtoMapper;

	public void applyDiscount(List<ProductDTO> products, int discountValue) {

		products.forEach(product -> {

			double price = product.getPrice() * ((100.0 - discountValue) / 100.0);
			product.setPrice(price);
		});
	}

	public void setDiscount(int value) {

		shopResearcherService.getDiscount().setValue(value);
	}

	public void resetDiscount() {

		shopResearcherService.getDiscount().setValue(0);
	}

	public List<ProductDTO> getProductsWithDiscount(String shopName, String category) {

		List<ProductDTO> productsDTO;
		List<Product> products = productRepositoryService.getSearchedProducts(shopName, category);
		DiscountDTO discountDTO = shopResearcherService.getDiscount();

		productsDTO = products.stream().map(product -> productDtoMapper.apply(product)).collect(Collectors.toList());

		this.applyDiscount(productsDTO, discountDTO.getValue());
		productsDTO.forEach(product -> product
				.setPrice(Double.parseDouble(ProductFormatter.formatProductPrice(product.getPrice()))));

		return productsDTO;
	}

}
