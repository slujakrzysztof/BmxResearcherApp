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

import com.bmxApp.enums.Part;
import com.bmxApp.enums.Shop;
import com.bmxApp.model.Discount;
import com.bmxApp.model.Product;
import com.bmxApp.model.ShopModel;
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

	static List<String> shopList = null;

	private boolean discountApplied = false;

	private Discount discountValue = new Discount();

	@GetMapping(value = "/search")
	public String searchProducts(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName) {
		if (!isDiscountApplied()) {
			mainControllerService.getProducts().clear();
			mainControllerService.setProductsSearching(shopName, category);
			mainControllerService.setProducts(shopName, category);
		}
		this.setDiscountApplied(false);
		model.addAttribute("products", mainControllerService.getProducts());
		mainControllerService.setCurrentShop(shopName);
		model.addAttribute("shopName", shopName);
		model.addAttribute("category", category.toLowerCase());
		model.addAttribute("discountValue", discountValue);
		return "products";
	}

/*	@PostMapping
	public String submitTest(Model model, ShopModel shopModel) {
		model.addAttribute("shopModel", shopModel);
		return "nic";
	}*/

	@PostMapping("/addProduct")
	public String addProductToBasket(@ModelAttribute("product") Product product, Model model,
			BindingResult bindingResult) {
		shoppingCartService.addProductToBasket(product.getId(), product.getShopName());
		return "redirect:/search?shop=" + mainControllerService.getCurrentShop() + "&category="
				+ Part.fromStringValue(product.getCategory()).toString();
	}

	@GetMapping("/main")
	public String showMainPage(Model model) {
		model.addAttribute("shopModel", new ShopModel());
		model.addAttribute("basketProducts", mainControllerService.getBasketProducts());
		return "main";
	}

	@GetMapping("/applyDiscount")
	public String applyDiscount(@ModelAttribute("discountValue") Discount discountValue, Model model) {
		String category = Part.fromStringValue(mainControllerService.getProducts().get(0).getCategory()).toString();
		mainControllerService.getProducts().clear();
		mainControllerService.setProducts(mainControllerService.getCurrentShop(), category);
		mainControllerService.applyDiscount(mainControllerService.getProducts(),
				(double) discountValue.getDiscountValue());
		this.setDiscountApplied(true);
		return "redirect:/search?shop=" + mainControllerService.getCurrentShop() + "&category=" + category;
	}

	private boolean isDiscountApplied() {
		return discountApplied;
	}

	private void setDiscountApplied(boolean discountApplied) {
		this.discountApplied = discountApplied;
	}

}
