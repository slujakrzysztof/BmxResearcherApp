package com.bmxApp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.bmxApp.dto.basketProduct.BasketProductDTO;
import com.bmxApp.dto.shopModel.ShopModelDTO;
import com.bmxApp.service.cart.ShoppingCartService;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;

	@GetMapping({ "/cart", "/cart/{shopName}" })
	public String showShoppingCart(@PathVariable(required = false) String shopName, Model model,
			HttpServletRequest request) {

		model.addAttribute("shopModel", new ShopModelDTO());
		model.addAttribute("totalPrice",
				shoppingCartService.formatPrice(shoppingCartService.getTotalPriceForShop(shopName)));
		model.addAttribute("totalPriceForBasketProduct", shoppingCartService.getTotalPriceForEachBasketProduct());
		model.addAttribute("totalDiscount", shoppingCartService.getTotalDiscount(shopName));
		model.addAttribute("finalPrice", shoppingCartService.getFinalPrice(shopName));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProductsInCart(shopName));
		model.addAttribute("currentURL", shoppingCartService.getCartURL(request));

		return "basket";
	}

	@DeleteMapping({ "/deleteProducts", "/cart/deleteProducts" })
	public String deleteBasketProducts() {

		shoppingCartService.deleteBasketProducts();
		return "redirect:/cart";
	}

	@DeleteMapping("/removeDropdownCartProducts")
	public RedirectView removeDropdownProducts(@RequestParam("values") String[] values,
			@RequestParam String currentURL) {

		RedirectView redirectView = new RedirectView();
		shoppingCartService.deleteBasketProducts();
		redirectView.setUrl(currentURL);
		return redirectView;
	}

	@PatchMapping("/changeQuantity")
	public String changeQuantity(@Nullable @RequestParam("quantityValue") String quantityValue,
			@Nullable @RequestParam("quantityController") String quantityController, @RequestParam("productId") String productId) {
	
		//TO CHANGE!!!
		
		boolean notNull = Optional.ofNullable(quantityValue).isPresent();
		int quantity = ((notNull) ? Integer.parseInt(quantityValue) : Integer.parseInt(quantityController));
		shoppingCartService.changeQuantity(Integer.parseInt(productId), quantity);
		return "redirect:/cart";
	}

	@DeleteMapping("/removeBasketProduct/{productId}")
	public RedirectView removeBasketProduct(@RequestParam String currentURL, @PathVariable int productId) {

		RedirectView redirectView = new RedirectView();
		shoppingCartService.deleteBasketProductByProductId(productId);
		redirectView.setUrl(currentURL);
		return redirectView;
	}

}
