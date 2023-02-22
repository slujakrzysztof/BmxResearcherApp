package com.bmxApp.controller;

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

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;
	private final ShoppingCartService shoppingCartService;
	
	
	@GetMapping(value = "/search")
	public String searchProducts(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName, HttpServletRequest request) {
		
		DiscountDTO discount = searchService.getShopResearcherService().getDiscount();
		
		searchService.searchProducts(shopName, category);

		model.addAttribute("products", searchService.getProductsWithDiscount(shopName, category));
		model.addAttribute("basketProducts", searchService.getBasketProducts());
		model.addAttribute("basketTotalPrice", searchService.getBasketTotalPrice());
		model.addAttribute("shopName", shopName);
		model.addAttribute("category", category.toLowerCase());
		model.addAttribute("discountValue", discount.getValue());
		model.addAttribute("currentURL", searchService.getSearchURL(request));
		
		searchService.resetDiscount();
		return "products";
	}
	
	@GetMapping(value="/cartSearch")
	public String search(@RequestParam("value") String searchValue, Model model, HttpServletRequest request) {

		DiscountDTO discount = searchService.getShopResearcherService().getDiscount();
		
		
		model.addAttribute("products", searchService.getRequestedItems(searchValue));
		model.addAttribute("discountValue", discount.getValue());
		model.addAttribute("currentURL", searchService.getSearchURL(request));
		model.addAttribute("basketProducts", searchService.getBasketProducts());
		model.addAttribute("basketTotalPrice", searchService.getBasketTotalPrice());
		return "";
	}
	

	@PostMapping("/applyDiscount")
	public RedirectView applyDiscount(@RequestParam("value") int value, @RequestParam("currentURL") String currentURL) {
		
		searchService.setDiscount(value);
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
			@RequestParam("currentURL") String currentURL,
			Model model,
			BindingResult bindingResult) {
		
		shoppingCartService.addProductToCart(dtoProduct.getProductName(), dtoProduct.getShopName());
		RedirectView redirectView = new RedirectView();
		redirectView.setUrl(currentURL);
		return redirectView;
	}
	
}
