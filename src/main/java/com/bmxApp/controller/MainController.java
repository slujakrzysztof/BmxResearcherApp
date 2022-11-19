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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bmxApp.enums.Part;
import com.bmxApp.enums.Shop;
import com.bmxApp.handler.ProductDatabaseHandler;
import com.bmxApp.model.ShopModel;
import com.bmxApp.model.ShopProduct;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.service.MainControllerService;

@Controller
@RequestMapping({ "/", "/main" })
public class MainController {

	@Autowired
	MainControllerService mainControllerService;

	PropertyReader propertyReader = PropertyReader.getInstance();

	static List<String> shopList = null;

	static {
		shopList = new ArrayList<>();
		shopList.add("bmxlife");
		shopList.add("avebmx");
	}

	// @RequestMapping(value = "/search")
	@GetMapping(value = "/search")
	public String searchProducts(Model model, @RequestParam("category") String category,
			@RequestParam("shop") String shopName) {
		propertyReader.setPropertyFilename("lang/res.properties");
		propertyReader.setConnection();
		mainControllerService.setResearcher(propertyReader.getProperty(category.toLowerCase()).toLowerCase(),
				shopName.toLowerCase(), 1, true);
		System.out.println("NAZWA PARTA: " + propertyReader.getProperty(category.toLowerCase()));
		model.addAttribute("products", mainControllerService.getDatabaseService().getProductsByCategoryAndShopName(
				propertyReader.getProperty(category.toLowerCase()), shopName.toLowerCase()));
		return "products";
	}

	@PostMapping("/main1")
	public String hello2(Model model, ShopModel shopModel) {
		model.addAttribute("shopModel", shopModel);
		System.out.println("222: " + shopModel.getShop());
		System.out.println("333: " + shopModel.getPartName());
		return "nic2";
	}

	// @RequestMapping(value = "/main")
	@PostMapping
	public String submitTest(Model model, ShopModel shopModel) {
		model.addAttribute("shopModel", shopModel);
		System.out.println(shopModel.getShop());

		return "nic";
	}

	// @RequestMapping(value = "/main")
	// @ResponseBody
	@GetMapping("/main")
	public String hello1(Model model) {
		// ModelAndView model = new ModelAndView("main");
		System.out.println(Part.BARS.getValue());
		model.addAttribute("shopModel", new ShopModel());
		// researcherControllerService.insertProduct();
		return "main";
	}

	/*
	 * @RequestMapping(value = "/main")
	 * 
	 * @ResponseBody
	 * 
	 * @PostMapping public String hello2(Model model, Shop shopp) {
	 * System.out.println("NAZWA SKLEPU: " + shopp.name());
	 * model.addAttribute("shopp", shopp); return "products"; }
	 */

	/*
	 * @RequestMapping(value = "/getNumber") public String hello() { return
	 * "Sieeeeeema: " + productDatabaseHandler.findByCategory("aaa").toString(); }
	 */

	/*
	 * @RequestMapping(value = "/main")
	 * 
	 * @ResponseBody public ModelAndView hello2() {
	 * 
	 * ModelAndView modelAndView = new ModelAndView();
	 * modelAndView.setViewName("main.html");
	 * 
	 * return modelAndView; //+
	 * productDatabaseHandler.findByCategory("aaa").toString(); }
	 */

}
