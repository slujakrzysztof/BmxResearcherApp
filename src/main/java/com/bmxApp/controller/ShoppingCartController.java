package com.bmxApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.shopModel.ShopModelDTO;
import com.bmxApp.service.cart.ShoppingCartService;

@Controller
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@GetMapping({ "/cart", "/cart/{shopName}" })
	public String showShoppingCart(@PathVariable(required = false) String shopName, Model model) {

		System.out.println("SHOP :" + shopName);
		
		model.addAttribute("shopModel", new ShopModelDTO());
		model.addAttribute("totalPrice", shoppingCartService.formatPrice(shoppingCartService.getTotalPriceForShop(shopName)));
		model.addAttribute("totalPriceForBasketProduct", shoppingCartService.getTotalPriceForEachBasketProduct());
		model.addAttribute("totalDiscount", shoppingCartService.getTotalDiscount(shopName));
		model.addAttribute("finalPrice", shoppingCartService.getFinalPrice(shopName));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProductsInCart(shopName));
		return "basket";
	}

	@GetMapping("/quantityChangedPlus/{productId}")
	public String changeQuantityPlus(@PathVariable String productId) {
		
		shoppingCartService.changeQuantity(Integer.parseInt(productId), 1);

		return "redirect:/cart";
	}

	@PostMapping({"/deleteProducts", "/cart/deleteProducts"})
	public String deleteBasketProducts() {
		
		shoppingCartService.deleteBasketProducts();
		return "basket";
	}
	
	@GetMapping("/removeDropdownProducts/{shopName}/{category}")
	public String removeDropdownProducts(@PathVariable String shopName, @PathVariable String category) {
		
		shoppingCartService.deleteBasketProducts();
		return "redirect:/" + shoppingCartService.getPage(shopName, category);
	}

	@GetMapping("/quantityChangedMinus/{productId}")
	public String changeQuantityMinus(@PathVariable String productId) {
		
		shoppingCartService.changeQuantity(Integer.parseInt(productId), -1);
		return "redirect:/cart";
	}

	@GetMapping("/removeProduct/{shopName}/{category}/{productId}")
	public String removeBasketProduct(@PathVariable String shopName, @PathVariable String category, @PathVariable String productId) {
		
		shoppingCartService.deleteBasketProductByProductId(Integer.parseInt(productId));
		return "redirect:/" + shoppingCartService.getPage(shopName,category);
	}

}
