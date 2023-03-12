package com.bmxApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.discount.DiscountService;
import com.bmxApp.service.filter.FilterService;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Controller
@Getter
@RequiredArgsConstructor
public class FilterController {

	private final FilterService filterService;
	private final ShoppingCartService shoppingCartService;
	private final DiscountService discountService;

	@GetMapping(value = "/filter")
	public String filterByShop(@Nullable @RequestParam("shop") String shop,
			@Nullable @RequestParam("category") String category, @RequestParam("searchValue") String searchValue,
			@Nullable @RequestParam("minPrice") Integer minPrice, @Nullable @RequestParam("maxPrice") Integer maxPrice,
			Model model) {
		
		filterService.setMinPrice(minPrice);
		filterService.setMaxPrice(maxPrice);
		filterService.setCategory(category);
		filterService.setShop(shop);
		
		model.addAttribute("products", filterService.getFilteredProducts(searchValue));
		model.addAttribute("discountValue", discountService.getDiscount());
		model.addAttribute("minPrice", minPrice);
		model.addAttribute("maxPrice", maxPrice);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());

		return "searchPage";
	}
	

}
