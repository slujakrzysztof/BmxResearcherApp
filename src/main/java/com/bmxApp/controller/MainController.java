package com.bmxApp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.shopModel.ShopModelDTO;
import com.bmxApp.enums.Part;
import com.bmxApp.enums.Shop;
import com.bmxApp.model.Product;
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

	//static List<String> shopList = null;

	//private boolean discountApplied = false;

	//private Discount discountValue = new Discount();

	@GetMapping(value = "/search")
	public String searchProducts(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName) {
		
		DiscountDTO discount = mainControllerService.getShopResearcherService().getDiscount();
		//DiscountDTO discount = mainControllerService.getShopResearcher().getDiscount();
		mainControllerService.searchProducts(shopName, category);
		mainControllerService.setCategory(category);
		mainControllerService.setCurrentShop(shopName);
		//if(!discount.isApplied()) {
		//	mainControllerService.getProducts().clear();
			//mainControllerService.setProductsSearching(shopName, category);
		//	mainControllerService.setProducts(shopName, category);
		//}
		//discount.setApplied(false);
		//shoppingCartService.setDiscountValue(discount);
		System.out.println("PROOOOOOOOOODUCTS: " + mainControllerService.getProductsWithDiscount(shopName, category));
		model.addAttribute("products", mainControllerService.getProductsWithDiscount(shopName, category));
		model.addAttribute("shopName", shopName);
		model.addAttribute("category", category.toLowerCase());
		model.addAttribute("discount", discount);
		return "products";
	}

	@PostMapping("/addProduct")
	public String addProductToBasket(@ModelAttribute("product") Product product, Model model,
			BindingResult bindingResult) {
		
		shoppingCartService.addProductToCart(product.getProductName(), product.getShopName(), product.getId());
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
	public String applyDiscount(@ModelAttribute("discountValue") DiscountDTO discountValue, Model model) {
		
		//String category = Part.fromStringValue(mainControllerService.getProducts().get(0).getCategory()).toString();
		DiscountDTO discount = mainControllerService.getShopResearcherService().getDiscount();
		discount.setValue(discountValue.getValue());
		
		//mainControllerService.getProducts().clear();
		//mainControllerService.setProducts(mainControllerService.getCurrentShop(), category);
		//mainControllerService.applyDiscount(mainControllerService.getProducts(),
			//	(double) discount.getValue());
		//this.setDiscountApplied(true);
		return "redirect:/search?shop=" + mainControllerService.getCurrentShop() + "&category=" + mainControllerService.getCategory();
	}


}
