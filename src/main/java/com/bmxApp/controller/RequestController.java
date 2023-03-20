package com.bmxApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.discount.DiscountService;
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
	private final DiscountService discountService;
	
	@GetMapping(value = "/requestProducts")
	public String search(@RequestParam("value") String searchValue, @Nullable @RequestParam("sortedBy") String sortBy,
			@Nullable @RequestParam("discountValue") String discountValue, Model model, HttpServletRequest request) {

		Optional<String> sortedBy = Optional.ofNullable(sortBy);
		Optional<String> discount = Optional.ofNullable(discountValue);

		sortedBy.ifPresentOrElse((value) -> {
			sortService.setSortedBy(!sortService.isSortedBy());
			List<ProductDTO> products = requestService.getSortedRequestedProducts(searchValue, sortBy,
					sortService.isSortedBy());
			
			model.addAttribute("products", products);
			
			discount.ifPresent(discValue -> model.addAttribute("products",
					discountService.getProductsWithDiscount(products, Integer.parseInt(discountValue))));
			
		}, () -> {
			
			List<ProductDTO> products = requestService.getRequestedProducts(searchValue);
			model.addAttribute("products", products);
			
			discount.ifPresent(discValue -> model.addAttribute("products",
					discountService.getProductsWithDiscount(products, Integer.parseInt(discountValue))));
		});

		model.addAttribute("discountValue", discountValue);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("currentUrl", requestService.getSearchURL(request));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());
		
		return "searchPage";
	}
	
}
