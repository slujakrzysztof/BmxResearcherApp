package com.bmxApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.shopModel.ShopModelDTO;
import com.bmxApp.model.product.Product;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.main.MainControllerService;

@Controller
@RequestMapping({ "/", "/main" })
public class MainController {

	@Autowired
	MainControllerService mainControllerService;

	PropertyReader propertyReader = PropertyReader.getInstance();

	@GetMapping("/main")
	public String showMainPage(Model model) {
				
		model.addAttribute("shopModel", new ShopModelDTO());
		model.addAttribute("basketProducts", mainControllerService.getBasketProducts());
		return "main";
	}

}
