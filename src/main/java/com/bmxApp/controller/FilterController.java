package com.bmxApp.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.discount.DiscountService;
import com.bmxApp.service.filter.FilterService;
import com.bmxApp.service.sort.SortService;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Controller
@Getter
@RequiredArgsConstructor
public class FilterController {

	private final FilterService filterService;
	private final ShoppingCartService shoppingCartService;
	private final DiscountService discountService;
	private final SortService sortService;

	@GetMapping(value = "/filter")
	public String filter(@Nullable @RequestParam("shop") String shop,
			@Nullable @RequestParam("category") String category, @RequestParam("searchValue") String searchValue,
			@Nullable @RequestParam("minPrice") Integer minPrice, @Nullable @RequestParam("maxPrice") Integer maxPrice,
			@Nullable @RequestParam("sortedBy") String sortedBy, Model model, HttpServletRequest request) {
		
		filterService.setMinPrice(minPrice);
		filterService.setMaxPrice(maxPrice);
		filterService.setCategory(category);
		filterService.setShop(shop);
		
		//System.out.println("URL: " + (request.getRequestURI() + "?" + request.getQueryString()));
		
		List<ProductDTO> products = filterService.getFilteredProducts(searchValue);
		
		if(sortedBy != null) products = sortService.sortProductDTO(sortedBy, products);

		model.addAttribute("products", products);
		model.addAttribute("discountValue", discountService.getDiscount());
		model.addAttribute("minPrice", minPrice);
		model.addAttribute("maxPrice", maxPrice);
		model.addAttribute("shopName", shop);
		model.addAttribute("categoryName", category);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("currentUrl", (request.getRequestURI() + "?" + request.getQueryString()));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());

		return "searchPage";
		
	}
	
	@GetMapping(value = "/sortFilter")
	public ModelAndView sortFilter(Model model, @RequestParam("currentUrl") String currentUrl, @RequestParam("sortedBy") String sortedBy) {
		
		
		return new ModelAndView("redirect:" + sortService.getSortUrl(currentUrl, sortedBy));

	}

}
