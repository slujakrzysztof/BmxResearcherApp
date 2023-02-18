package com.bmxApp.service.main;

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
import com.bmxApp.repository.BasketProductRepository;
import com.bmxApp.repository.ProductRepository;
import com.bmxApp.researcher.ShopResearcherService;
import com.bmxApp.service.basketProduct.BasketProductRepositoryService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@Getter
@RequiredArgsConstructor
public class MainControllerService {

	final BasketProductRepositoryService basketProductRepositoryService;

	
	public ArrayList<BasketProductDTO> getBasketProducts() {

		return basketProductRepositoryService.getBasketProductsDTO();
	}

}
