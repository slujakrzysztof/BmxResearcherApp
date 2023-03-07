package com.bmxApp.service.filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.FilterItem;
import com.bmxApp.enums.SortingItem;
import com.bmxApp.formatter.product.ProductFormatter;
import com.bmxApp.mapper.basketProduct.BasketProductDTOMapper;
import com.bmxApp.mapper.product.ProductDTOMapper;
import com.bmxApp.model.basketProduct.BasketProduct;
import com.bmxApp.model.product.Product;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.database.BasketProductRepositoryService;
import com.bmxApp.service.database.ProductRepositoryService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@Getter
@RequiredArgsConstructor
public class FilterService {

	private final ProductRepositoryService productRepositoryService;
	private final BasketProductRepositoryService basketProductRepositoryService;
	private final ProductDTOMapper productDTOMapper;
	private final BasketProductDTOMapper basketProductDTOMapper;

	public List<ProductDTO> getFilteredProducts(String value, String filter, String filterValue) {

		List<Product> products = productRepositoryService.getRequestedItem(value);
		List<ProductDTO> productsDTO;
		Predicate<Product> filteredParameter = this.getPredicate(filter, filterValue);

		productsDTO = products.stream().filter(product -> filteredParameter.test(product))
				.map(product -> productDTOMapper.apply(product)).collect(Collectors.toList());

		return productsDTO;
	}
	
	private Predicate<Product> getPredicate(String filter, String filterValue){
		
		Predicate<Product> predicate = null;
		FilterItem filterItem = FilterItem.fromString(filter);
		
		switch(filterItem) {
		
		case SHOP : {
			predicate = product -> product.getShopName().equalsIgnoreCase(filter);
			break;
		}
		//TO CHANGE
		case PRICE : {
			String[] rangeString = filterValue.split("-");
			double[] range = new double[2];
			
			for(int counter=0; counter < rangeString.length; counter++)
				range[counter] = Double.parseDouble(rangeString[counter]);
			
			predicate = product -> (product.getPrice() >= range[0] && product.getPrice() < range[1]);
		}
		case PRODUCER:
			break;
		default:
			break;
		
		}
		
		return predicate;
	}

}
