package com.bmxApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.discount.DiscountService;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DiscountController {

	private final DiscountService discountService;
	private final ShoppingCartService shoppingCartService;

	@GetMapping("/searchDiscount")
	public String searchProductsWithDiscount(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName, @RequestParam("discountValue") String discountValue,
			@Nullable @RequestParam("sortBy") String sortBy, HttpServletRequest request) {
		
		if (discountValue.isEmpty())
			discountValue = "0";

		discountService.setDiscount(Integer.parseInt(discountValue));
		
		model.addAttribute("products", discountService.getProductsWithDiscount(shopName, category));
		model.addAttribute("basketProducts", searchService.getBasketProducts());
		model.addAttribute("basketTotalPrice", searchService.getBasketTotalPrice());
		model.addAttribute("shopName", shopName);
		model.addAttribute("category", category.toLowerCase());
		model.addAttribute("discountValue", discount.getValue());
		model.addAttribute("currentURL", searchService.getSearchURL(request));
		
		return "products";
	}

}
