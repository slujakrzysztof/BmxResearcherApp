package com.bmxApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.bmxApp.model.BasketProduct;
import com.bmxApp.model.Product;
import com.bmxApp.service.ShoppingCartService;

@Controller
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;

	@GetMapping({"/cart" , "/cart/{shop}"})
	public String showShoppingCart(@PathVariable(required=false) String shop, Model model) {
		List<BasketProduct> basketProducts;
		if(shop == null) basketProducts = shoppingCartService.getAllProducts();
		else basketProducts = shoppingCartService.getBasketProductsByShopName(shop);
		System.out.println("SKLEPIK: " + shop);
		System.out.println("LACZNY HAJS: " + shoppingCartService.getTotalPriceForProduct(1));
		model.addAttribute("totalPriceByProduct", shoppingCartService.getTotalPriceForProduct(1));
		model.addAttribute("totalPrice", shoppingCartService.getTotalPrice());
		model.addAttribute("basketProducts", basketProducts);
		return "cart";
	}

	@PostMapping("/quantityChangedPlus")
	public String changeQuantityPlus(@ModelAttribute("basketProductId") BasketProduct basketProduct, Model model,
			BindingResult bindingResult) {

		int quantity = shoppingCartService.getQuantity(basketProduct.getId()) + 1;
		shoppingCartService.changeQuantity(basketProduct.getId(), quantity);

		return "redirect:/cart";
	}

	@PostMapping("/quantityChangedMinus")
	public String changeQuantityMinus(@ModelAttribute("basketProductId") BasketProduct basketProduct, Model model,
			BindingResult bindingResult) {

		int quantity = shoppingCartService.getQuantity(basketProduct.getId()) - 1;
		shoppingCartService.changeQuantity(basketProduct.getId(), quantity);

		return "redirect:/cart";
	}
	

}
