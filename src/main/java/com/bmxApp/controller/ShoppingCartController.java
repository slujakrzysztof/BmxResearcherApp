package com.bmxApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;
import com.bmxApp.service.ShoppingCartService;

@Controller
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;

	@GetMapping("/cart")
	public String showShoppingCart(Model model) {
		List<BasketProduct> basketProducts = shoppingCartService.getAllProducts();
		System.out.println("LACZNY HAJS: " + shoppingCartService.getTotalPriceForProduct(1));
		model.addAttribute("totalPriceByProduct", shoppingCartService.getTotalPriceForProduct(1));
		model.addAttribute("totalPrice", shoppingCartService.getTotalPrice());
		model.addAttribute("basketProducts", basketProducts);
		return "cart";
	}



}
