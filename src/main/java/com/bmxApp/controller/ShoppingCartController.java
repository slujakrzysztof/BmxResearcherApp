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
import com.bmxApp.service.ShoppingCartService;

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

	@PostMapping("/quantityChangedPlus")
	public String changeQuantityPlus(@ModelAttribute("dtoBasketProduct") BasketProductDTO dtoBasketProduct, Model model,
			BindingResult bindingResult) {

		//System.out.println("BPROD : " + dtoBasketProduct.getProductId());
		
		shoppingCartService.changeQuantity(dtoBasketProduct, 1);

		return "redirect:/cart";
	}

	@PostMapping({"/deleteProducts", "/cart/deleteProducts"})
	public String deleteBasketProducts() {
		
		shoppingCartService.deleteBasketProducts();
		return "basket";
	}

	@PostMapping("/quantityChangedMinus")
	public String changeQuantityMinus(@ModelAttribute("basketProduct") BasketProductDTO basketProduct, Model model,
			BindingResult bindingResult) {
		
		shoppingCartService.changeQuantity(basketProduct, -1);
		return "redirect:/cart";
	}

	@GetMapping("/removeProduct/{page}/{productId}")
	public String removeBasketProduct(@PathVariable String page, @PathVariable String productId) {
		
		shoppingCartService.deleteBasketProductByProductId(Integer.parseInt(productId));
		return "redirect:/" + page;
	}

}
