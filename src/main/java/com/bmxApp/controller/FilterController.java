package com.bmxApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.creator.PathCreator;
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
			@Nullable @RequestParam("sortedBy") String sortedBy,
			@Nullable @RequestParam("discountValue") String discountValue, Model model, HttpServletRequest request) {

		Optional<String> discount = Optional.ofNullable(discountValue);

		filterService.setMinPrice(minPrice);
		filterService.setMaxPrice(maxPrice);
		filterService.setCategory(category);
		filterService.setShop(shop);

		List<ProductDTO> products = filterService.getFilteredProducts(searchValue);

		if (sortedBy != null) {
			sortService.setSortedBy(!sortService.isSortedBy());
			products = sortService.sortProductDTO(sortedBy, products);

		}

		if (discount != null)
			products = discountService.getProductsWithDiscount(products);

		model.addAttribute("products", products);
		model.addAttribute("discountValue", discountService.getDiscount());
		model.addAttribute("minPrice", minPrice);
		model.addAttribute("maxPrice", maxPrice);
		model.addAttribute("shopName", shop);
		model.addAttribute("categoryName", category);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("currentUrl", PathCreator.getCurrentUrl(request));
		model.addAttribute("basketProducts", shoppingCartService.getBasketProducts(null));
		model.addAttribute("basketTotalPrice", shoppingCartService.getTotalPrice());

		return "searchPage";

	}

}
