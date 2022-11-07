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
import com.bmxApp.model.ShopProduct;
import com.bmxApp.model.Test;
import com.bmxApp.service.MainControllerService;
import com.bmxApp.service.ResearcherControllerService;

@Controller
//@RequestMapping("/main")
public class MainController {

	@Autowired
	MainControllerService mainControllerService;

	@Autowired
	ResearcherControllerService researcherControllerService;

	@Autowired
	Test test;

	static List<String> shopList = null;

	static {
		shopList = new ArrayList<>();
		shopList.add("bmxlife");
		shopList.add("avebmx");
	}

	@RequestMapping(value = "/search")
	// @PostMapping("/search")
	public String searchProducts(Model model, @ModelAttribute("shop") Shop shop) {
		researcherControllerService.setResearcher("ramy", "bmxlife", 1, true);// this.mainControllerService.getPartSearched());
		// model = new ModelAndView("products");
		// model.addAttribute("products",
		// researcherControllerService.getProducts("ramy", "bmxlife"));

		return "products";
	}

	// @RequestMapping(value = "/main")
	@PostMapping(path = "/main1")
	public String submitTest(@ModelAttribute("test") Test test, Model model, BindingResult result) {
		//model.addAttribute("test", test.toString());
		System.out.println("SHOPPP: " + test.toString());

		return "redirect:/main1";
	}

	// @RequestMapping(value = "/main")
	// @ResponseBody
	@GetMapping(path = "/main1")
	public String hello1(Model model) {
		// ModelAndView model = new ModelAndView("main");
		System.out.println(Part.BARS.getValue());
		model.addAttribute("shopList", shopList);
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
