package com.bmxApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.model.product.Product;
import com.bmxApp.service.basketProduct.BasketProductRepositoryService;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.search.SearchService;

@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private BasketProductRepositoryService basketProductRepositoryService;
	
	@GetMapping(value = "/search")
	public String searchProducts(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName) {
		
		DiscountDTO discount = searchService.getShopResearcherService().getDiscount();

		searchService.searchProducts(shopName, category);
		searchService.setCategory(category);
		searchService.setCurrentShop(shopName);

		model.addAttribute("products", searchService.getProductsWithDiscount(shopName, category));
		model.addAttribute("basketProducts", basketProductRepositoryService.getBasketProductsDTO());
		model.addAttribute("shopName", shopName);
		model.addAttribute("category", category.toLowerCase());
		model.addAttribute("discount", discount);
		model.addAttribute("discountValue", discount.getValue());
		searchService.resetDiscount();
		return "products";
	}
	

	@GetMapping("/applyDiscount")
	public String applyDiscount(@ModelAttribute("value") int value, Model model) {

		searchService.setDiscount(value);
		
		return "redirect:/search?shop=" + searchService.getCurrentShop() + "&category=" + searchService.getCategory();
	}
	
	@GetMapping("/resetDiscount")
	public String applyDiscount(Model model) {

		searchService.resetDiscount();
		
		return "redirect:/search?shop=" + searchService.getCurrentShop() + "&category=" + searchService.getCategory();
	}
	
	@PostMapping("/addProduct")
	public String addProductToBasket(@ModelAttribute("product") ProductDTO dtoProduct, Model model,
			BindingResult bindingResult) {
				
		shoppingCartService.addProductToCart(dtoProduct.getProductName(), dtoProduct.getShopName());
		
		return "redirect:/search?shop=" + dtoProduct.getShopName() + "&category="
				+ dtoProduct.getCategory();
	}
}
