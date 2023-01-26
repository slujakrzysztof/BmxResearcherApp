package com.bmxApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.shopModel.ShopModelDTO;
import com.bmxApp.model.product.Product;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.service.MainControllerService;
import com.bmxApp.service.ShoppingCartService;

@Controller
@RequestMapping({ "/", "/main" })
public class MainController {

	@Autowired
	MainControllerService mainControllerService;

	PropertyReader propertyReader = PropertyReader.getInstance();

	@Autowired
	private ShoppingCartService shoppingCartService;


	@GetMapping(value = "/search")
	public String searchProducts(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName) {
		
		DiscountDTO discount = mainControllerService.getShopResearcherService().getDiscount();

		mainControllerService.searchProducts(shopName, category);
		mainControllerService.setCategory(category);
		mainControllerService.setCurrentShop(shopName);

		model.addAttribute("products", mainControllerService.getProductsWithDiscount(shopName, category));
		model.addAttribute("shopName", shopName);
		model.addAttribute("category", category.toLowerCase());
		model.addAttribute("discount", discount);
		model.addAttribute("discountValue", discount.getValue());
		mainControllerService.resetDiscount();
		return "products";
	}

	@PostMapping("/addProduct")
	public String addProductToBasket(@ModelAttribute("product") Product product, Model model,
			BindingResult bindingResult) {
		
		shoppingCartService.addProductToCart(product.getProductName(), product.getShopName());
		return "redirect:/search?shop=" + product.getShopName() + "&category="
				+ product.getCategory();
	}

	@GetMapping("/main")
	public String showMainPage(Model model) {
				
		model.addAttribute("shopModel", new ShopModelDTO());
		model.addAttribute("basketProducts", mainControllerService.getBasketProducts());
		return "main";
	}

	@GetMapping("/applyDiscount")
	public String applyDiscount(@ModelAttribute("value") int value, Model model) {

		mainControllerService.setDiscount(value);
		
		return "redirect:/search?shop=" + mainControllerService.getCurrentShop() + "&category=" + mainControllerService.getCategory();
	}
	
	@GetMapping("/resetDiscount")
	public String applyDiscount(Model model) {

		mainControllerService.resetDiscount();
		
		return "redirect:/search?shop=" + mainControllerService.getCurrentShop() + "&category=" + mainControllerService.getCategory();
	}


}
