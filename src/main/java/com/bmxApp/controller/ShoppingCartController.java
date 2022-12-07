package com.bmxApp.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.bmxApp.model.BasketProduct;
import com.bmxApp.service.ShoppingCartService;

@Controller
public class ShoppingCartController {

	List<BasketProduct> basketProducts;
	Map<Integer, Float> basketProductsPrices = new TreeMap<>();

	@Autowired
	private ShoppingCartService shoppingCartService;

	@GetMapping({ "/cart", "/cart/{shop}" })
	public String showShoppingCart(@PathVariable(required = false) String shop, Model model) {

		if (shop == null)
			basketProducts = shoppingCartService.getAllProducts();
		else
			basketProducts = shoppingCartService.getBasketProductsByShopName(shop);

		for (BasketProduct bProduct : basketProducts) {
			basketProductsPrices.put(bProduct.getId(), shoppingCartService.getTotalPriceForProduct(bProduct.getId()));
			System.out.println(basketProductsPrices.toString());
		}

		System.out.println("SKLEPIK: " + shop);
		//System.out.println("LACZNY HAJS: " + shoppingCartService.getTotalPriceForProduct(1));
		model.addAttribute("totalPriceByProduct", basketProductsPrices);
		model.addAttribute("totalPrice", shoppingCartService.getTotalPrice(shop));
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

	@PostMapping("/deleteProducts")
	public String deleteBasketProducts() {
		shoppingCartService.deleteProducts();
		return "cart";
	}

	@PostMapping("/quantityChangedMinus")
	public String changeQuantityMinus(@ModelAttribute("basketProductId") BasketProduct basketProduct, Model model,
			BindingResult bindingResult) {

		int quantity = shoppingCartService.getQuantity(basketProduct.getId()) - 1;
		shoppingCartService.changeQuantity(basketProduct.getId(), quantity);

		return "redirect:/cart";
	}

}
