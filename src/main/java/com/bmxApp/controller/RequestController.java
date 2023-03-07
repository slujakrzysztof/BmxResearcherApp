package com.bmxApp.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.search.RequestService;
import com.bmxApp.service.sort.SortService;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RequestController {

	private final SortService sortService;
	private final ShoppingCartService shoppingCartService;
	private final RequestService requestService;
	
	@GetMapping(value = "/requestProducts")
	public String search(@RequestParam("value") String searchValue, @Nullable @RequestParam("sortBy") String sortBy,
			Model model, HttpServletRequest request) {

		Optional<String> sortedBy = Optional.ofNullable(sortBy);

		sortedBy.ifPresentOrElse((value) -> {
			sortService.setSortedBy(!sortService.isSortedBy());
			model.addAttribute("products", requestService.getSortedRequestedProducts(searchValue, sortBy,
					sortService.isSortedBy()));
		}, () -> {
			model.addAttribute("products", requestService.getRequestedProducts(searchValue));
		});

		model.addAttribute("discountValue", "0");
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("currentURL", requestService.getSearchURL(request));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());
		
		return "searchPage";
	}
	
}
