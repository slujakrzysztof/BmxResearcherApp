package com.bmxApp.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.discount.DiscountService;
import com.bmxApp.service.search.SearchService;
import com.bmxApp.service.sort.SortService;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DiscountController {

	private final DiscountService discountService;
	private final ShoppingCartService shoppingCartService;
	private final SearchService searchService;
	private final SortService sortService;

	@GetMapping("/searchDiscount")
	public String searchProductsWithDiscount(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName, @Nullable @RequestParam("discountValue") String discountValue,
			@Nullable @RequestParam("sortBy") String sortBy, HttpServletRequest request) {

		Optional<String> sortedBy = Optional.ofNullable(sortBy);

		if (discountValue.isEmpty() || discountValue.equalsIgnoreCase("0")) {

			discountService.setDiscount(Integer.parseInt(discountValue));
			return "redirect:" + searchService.getInitialSearchURL();
		}

		discountService.setDiscount(Integer.parseInt(discountValue));

		sortedBy.ifPresentOrElse((value) -> {
			sortService.setSortedBy(!sortService.isSortedBy());
			model.addAttribute("products", discountService.getSortedProductsWithDiscount(shopName, category, sortBy,
					sortService.isSortedBy()));
		}, () -> {
			model.addAttribute("products", discountService.getProductsWithDiscount(shopName, category));
		});

		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());
		model.addAttribute("shopName", shopName);
		model.addAttribute("category", category.toLowerCase());
		model.addAttribute("discountValue", discountService.getDiscount());
		model.addAttribute("currentURL", searchService.getSearchURL(request));

		return "products";
	}

	@GetMapping(value = "/requestProductsDiscount")
	public String search(@RequestParam("value") String searchValue, @Nullable @RequestParam("sortBy") String sortBy,
			@RequestParam("discountValue") String discountValue, Model model, HttpServletRequest request) {

		Optional<String> sortedBy = Optional.ofNullable(sortBy);

		if (discountValue.isEmpty() || discountValue.equalsIgnoreCase("0")) {

			discountValue = "0";
			discountService.setDiscount(Integer.parseInt(discountValue));
			return "redirect/" + searchService.getInitialSearchURL();
		}

		discountService.setDiscount(Integer.parseInt(discountValue));

		sortedBy.ifPresentOrElse((value) -> {
			sortService.setSortedBy(!sortService.isSortedBy());
			model.addAttribute("products", discountService.getSortedRequestedProductsWithDiscount(searchValue, sortBy,
					sortService.isSortedBy()));
		}, () -> {
			model.addAttribute("products", discountService.getRequestedProductsWithDiscount(searchValue));
		});

		model.addAttribute("discountValue", discountService.getDiscount());
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("currentURL", searchService.getSearchURL(request));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());

		return "searchPage";
	}
}
