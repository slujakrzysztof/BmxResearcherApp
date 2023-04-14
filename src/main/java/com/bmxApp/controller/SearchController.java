package com.bmxApp.controller;

import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.bmxApp.creator.PathCreator;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.comparison.ComparisonService;
import com.bmxApp.service.search.SearchService;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;
	private final ShoppingCartService shoppingCartService;
	private final ComparisonService comparisonService;

	@GetMapping(value = "/search")
	public String searchProducts(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName, @Nullable @RequestParam("sortedBy") String sortBy,
			@Nullable @RequestParam("discountValue") String discountValue, HttpServletRequest request) {


		Optional<String> discount = Optional.ofNullable(discountValue);
		
		searchService.searchProducts(shopName, category);

		discount.ifPresent(disc -> model.addAttribute("discountValue", discountValue));
		model.addAttribute("products", searchService.getProducts(shopName, category, sortBy, discountValue, request));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts());
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());
		model.addAttribute("shopName", shopName);
		model.addAttribute("category", category.toLowerCase());
		model.addAttribute("currentUrl", PathCreator.createSearchUri(request));
		model.addAttribute("comparatorFull", comparisonService.isComparatorFull());

		return "products";
	}

}
