package com.bmxApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.discount.DiscountService;
import com.bmxApp.service.search.SearchService;
import com.bmxApp.service.sort.SortService;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;
	private final ShoppingCartService shoppingCartService;
	private final SortService sortService;
	private final DiscountService discountService;

	@GetMapping(value = "/search")
	public String searchProducts(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName, @Nullable @RequestParam("sortedBy") String sortBy,
			@Nullable @RequestParam("discountValue") String discountValue, HttpServletRequest request) {

		Optional<String> sortedBy = Optional.ofNullable(sortBy);
		Optional<String> discount = Optional.ofNullable(discountValue);

		searchService.searchProducts(shopName, category);

		sortedBy.ifPresentOrElse((value) -> {

			sortService.setSortedBy(!sortService.isSortedBy());
			List<ProductDTO> products = searchService.getSortedProducts(shopName, category, sortBy,
					sortService.isSortedBy());

			model.addAttribute("products", products);
			discount.ifPresent(discValue -> model.addAttribute("products",
					discountService.getProductsWithDiscount(products, Integer.parseInt(discountValue))));

		}, () -> {

			List<ProductDTO> products = searchService.getProducts(shopName, category);

			model.addAttribute("products", products);

			discount.ifPresent(discValue -> model.addAttribute("products",
					discountService.getProductsWithDiscount(products, Integer.parseInt(discountValue))));

			searchService.setInitialSearchURL(searchService.getSearchURL(request));
		});

		discount.ifPresentOrElse(disc -> model.addAttribute("discountValue", discountValue),
				() -> model.addAttribute("discountValue", "0"));

		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());
		model.addAttribute("shopName", shopName);
		model.addAttribute("category", category.toLowerCase());
		model.addAttribute("currentURL", searchService.getSearchURL(request));

		return "products";
	}

}
