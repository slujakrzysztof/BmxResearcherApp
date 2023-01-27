package com.bmxApp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.Soundbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.Part;
import com.bmxApp.enums.Shop;
import com.bmxApp.manager.PropertyManager;
import com.bmxApp.mapper.basketProduct.BasketProductMapper;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.basketProduct.BasketProduct;
import com.bmxApp.model.product.Product;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.repository.ProductRepository;
import com.bmxApp.researcher.ShopResearcherService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Service
@Getter
@Setter
@NoArgsConstructor
public class MainControllerService {

	@Autowired
	ProductRepositoryService productRepositoryService;

	@Autowired
	BasketProductRepositoryService basketProductRepositoryService;



	private String language = "polish";

	//List<Product> products = new ArrayList<Product>();

	@Autowired(required = false)
	ShopResearcherService shopResearcherService;

	public ArrayList<BasketProductDTO> getBasketProducts() {

		ArrayList<BasketProduct> basketProductsList = this.basketProductRepositoryService.getBasketProducts(); 
		ArrayList<BasketProductDTO> basketProductsDtoList = new ArrayList<>();
		
		basketProductsList.forEach(basketProduct -> {
			BasketProductDTO dtoBasketProduct = BasketProductMapper.mapToBasketProductDTO(basketProduct);
			basketProductsDtoList.add(dtoBasketProduct);
		});
		
		return basketProductsDtoList;
	}

}
