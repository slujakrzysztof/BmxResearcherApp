package com.bmxApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.filter.FilterService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Controller
@Getter
@RequiredArgsConstructor
public class FilterController {

	private final FilterService filterService;
	private final ShoppingCartService shoppingCartService;

	
	@PostMapping(value = "/filter")
	public String filterByShop(@RequestParam("value") String searchValue, @RequestParam("shop") String shopName, @RequestParam("currentURL") String currentURL, Model model) {
		
		DiscountDTO discount = filterService.getShopResearcherService().getDiscount();
		
		model.addAttribute("products", filterService.getFilteredItemsWithDiscountByShopName(searchValue, shopName, discount));
		model.addAttribute("discountValue", discount.getValue());
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("currentURL", currentURL);
		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());

		return "searchPage";
	}
	
}
