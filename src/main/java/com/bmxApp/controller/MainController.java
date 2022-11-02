package com.bmxApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bmxApp.enums.Shop;
import com.bmxApp.handler.ProductDatabaseHandler;
import com.bmxApp.model.ShopProduct;
import com.bmxApp.service.MainControllerService;
import com.bmxApp.service.ResearcherControllerService;


@Controller
//@RequestMapping("/main")
public class MainController {

	@Autowired
	MainControllerService mainControllerService;

	@Autowired
	ResearcherControllerService researcherControllerService;

	@RequestMapping(value = "/search")
	//@PostMapping
	public String searchProducts(Model model) {
		researcherControllerService.setResearcher("ramy", "bmxlife", 1, true);//this.mainControllerService.getPartSearched());
		//model = new ModelAndView("products");
		model.addAttribute("products", researcherControllerService.getProducts("ramy", "bmxlife"));
		return "products";
	}
	


	@RequestMapping(value = "/main")
	//@ResponseBody
	@GetMapping
	public String hello1(Model model, Shop shopp) {
		//ModelAndView model = new ModelAndView("main");

		//researcherControllerService.insertProduct();
		return "main";
	}
	
	@RequestMapping(value = "/main")
	@ResponseBody
	@PostMapping
	public String hello2(Model model, Shop shopp) {
		System.out.println("NAZWA SKLEPU: " + shopp.name());
        model.addAttribute("shopp", shopp);
        return "products";
	}

	/*
	 * @RequestMapping(value = "/getNumber") public String hello() { return
	 * "Sieeeeeema: " + productDatabaseHandler.findByCategory("aaa").toString(); }
	 */

	/*@RequestMapping(value = "/main")
	@ResponseBody
	public ModelAndView hello2() {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("main.html");

		return modelAndView;
//+ productDatabaseHandler.findByCategory("aaa").toString();
	}*/

}
