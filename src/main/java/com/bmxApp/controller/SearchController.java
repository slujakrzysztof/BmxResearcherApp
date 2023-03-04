package com.bmxApp.controller;

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
import com.bmxApp.service.search.SearchService;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;
	private final ShoppingCartService shoppingCartService;

	@GetMapping(value = "/search")
	public String searchProducts(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName, @Nullable @RequestParam("sortBy") String sortBy,
			HttpServletRequest request) {

		DiscountDTO discount = searchService.getShopResearcherService().getDiscount();
		Optional<String> sortedBy = Optional.ofNullable(sortBy);

		searchService.searchProducts(shopName, category);

		sortedBy.ifPresentOrElse((value) -> {
			searchService.setSortedBy(!searchService.isSortedBy());
			model.addAttribute("products", searchService.getSortedProducts(shopName, category, sortBy,
					searchService.isSortedBy()));
		}, () -> {
			model.addAttribute("products", searchService.getProducts(shopName, category));
		});

		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());
		model.addAttribute("shopName", shopName);
		model.addAttribute("category", category.toLowerCase());
		model.addAttribute("discountValue", discount.getValue());
		model.addAttribute("currentURL", searchService.getSearchURL(request));

		searchService.resetDiscount();
		return "products";
	}

	@GetMapping(value = "/requestProducts")
	public String search(@RequestParam("value") String searchValue, @Nullable @RequestParam("sortBy") String sortBy,
			Model model, HttpServletRequest request) {

		DiscountDTO discount = searchService.getShopResearcherService().getDiscount();
		Optional<String> sortedBy = Optional.ofNullable(sortBy);

		sortedBy.ifPresentOrElse((value) -> {
			searchService.setSortedBy(!searchService.isSortedBy());
			model.addAttribute("products", searchService.getSortedRequestedProducts(searchValue, sortBy,
					searchService.isSortedBy()));
		}, () -> {
			model.addAttribute("products", searchService.getRequestedProducts(searchValue));
		});

		model.addAttribute("discountValue", discount.getValue());
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("currentURL", searchService.getSearchURL(request));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());
		return "searchPage";
	}

	@PostMapping("/applyDiscount")
	public RedirectView applyDiscount(@RequestParam("value") String value,
			@RequestParam("currentURL") String currentURL) {

		if (value.isEmpty())
			value = "0";

		searchService.setDiscount(Integer.parseInt(value));
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(currentURL);
		return redirectView;
	}

	@GetMapping("/resetDiscount")
	public RedirectView applyDiscount(@RequestParam("currentURL") String currentURL) {

		searchService.resetDiscount();
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(currentURL);
		return redirectView;
	}

	@PostMapping("/addProduct")
	public RedirectView addProductToBasket(@ModelAttribute("product") ProductDTO dtoProduct,
			@RequestParam("currentURL") String currentURL, Model model, BindingResult bindingResult) {

		shoppingCartService.addProductToCart(dtoProduct.getProductName(), dtoProduct.getShopName());
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(currentURL);
		return redirectView;
	}

}
