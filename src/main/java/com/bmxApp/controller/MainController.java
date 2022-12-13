package com.bmxApp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.enums.Part;
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


	@GetMapping(value = "/search")
	public String searchProducts(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName) {
		mainControllerService.setResearcher(Part.fromString(category).getValue(shopName),
				shopName.toLowerCase(), 1, true);
		model.addAttribute("products", mainControllerService.getDatabaseService().getProductsByCategoryAndShopName(
				Part.fromString(category).getValue(shopName), shopName.toLowerCase()));
		model.addAttribute("shopName", shopName);
		model.addAttribute("category", category.toLowerCase());
		return "products";
	}

	@PostMapping
	public String submitTest(Model model, ShopModel shopModel) {
		model.addAttribute("shopModel", shopModel);
		System.out.println(shopModel.getShop());

		return "nic";
	}

	@PostMapping("/addProduct")
	public String addProductToBasket(@ModelAttribute("product") Product product, Model model,
			BindingResult bindingResult) {
		shoppingCartService.addProductToBasket(product.getId(), product.getShopName());
		return "redirect:/search?shop=" + product.getShopName() + "&category="
				+  Part.fromStringValue(product.getCategory()).toString().toLowerCase();
	}
	

	
	@GetMapping("/main")
	public String showMainPage(Model model) {
		model.addAttribute("shopModel", new ShopModel());
		model.addAttribute("basketProducts", mainControllerService.getBasketProducts());
		return "main";
	}

}
