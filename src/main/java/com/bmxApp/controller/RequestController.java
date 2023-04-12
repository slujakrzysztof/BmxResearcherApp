package com.bmxApp.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.creator.PathCreator;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.search.RequestService;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RequestController {

	private final ShoppingCartService shoppingCartService;
	private final RequestService requestService;

	
	@GetMapping(value = "/requestProducts")
	public String search(@RequestParam("value") String searchValue, @Nullable @RequestParam("sortedBy") String sortBy,
			@Nullable @RequestParam("discountValue") String discountValue, Model model, HttpServletRequest request) {

		model.addAttribute("products", requestService.getProducts(searchValue, sortBy, discountValue));
		model.addAttribute("discountValue", discountValue);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("currentUrl", PathCreator.getCurrentUrl(request));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());
		
		return "searchPage";
	}
	
}
