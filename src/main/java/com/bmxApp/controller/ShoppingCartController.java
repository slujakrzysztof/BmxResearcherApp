package com.bmxApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
		
		model.addAttribute("shopModel", new ShopModelDTO());
		model.addAttribute("totalPrice", shoppingCartService.formatPrice(shoppingCartService.getTotalPriceForShop(shopName)));
		model.addAttribute("totalPriceForBasketProduct", shoppingCartService.getTotalPriceForEachBasketProduct());
		model.addAttribute("totalDiscount", shoppingCartService.getTotalDiscount(shopName));
		model.addAttribute("finalPrice", shoppingCartService.getFinalPrice(shopName));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProductsInCart(shopName));
		return "basket";
	}

	@DeleteMapping({"/deleteProducts", "/cart/deleteProducts"})
	public String deleteBasketProducts() {
		
		shoppingCartService.deleteBasketProducts();
		return "basket";
	}
	
	@DeleteMapping("/removeDropdownProducts/{shopName}/{category}")
	public String removeDropdownProducts(@PathVariable String shopName, @PathVariable String category) {
		
		shoppingCartService.deleteBasketProducts();
		return "redirect:/" + shoppingCartService.getPage(shopName, category);
	}
	
	@PatchMapping("/changeQuantity")
	public String changeQuantity(@RequestParam("quantityValue") String quantityValue, @RequestParam("productId") String productId) {
		
		shoppingCartService.changeQuantity(Integer.parseInt(productId), Integer.parseInt(quantityValue));
		return "redirect:/cart";
	}

	@DeleteMapping("/removeProduct")
	public String removeBasketProduct(@RequestParam("values") String[] values) {
		
		shoppingCartService.deleteBasketProductByProductId(Integer.parseInt(values[0]));
		return "redirect:/" + shoppingCartService.getPage(values[1]);
	}

}
