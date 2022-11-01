package com.bmxApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
	@PostMapping
	public ModelAndView searchProducts() {
		researcherControllerService.setResearcher("kierownice", "bmxlife", 1, true);//this.mainControllerService.getPartSearched());
		ModelAndView model = new ModelAndView("main");
		return model;
	}

	@RequestMapping(value = "/products")
	public ModelAndView hello1() {
		ModelAndView model = new ModelAndView("main");
		researcherControllerService.insertProduct();
		return model;
	}

	/*
	 * @RequestMapping(value = "/getNumber") public String hello() { return
	 * "Sieeeeeema: " + productDatabaseHandler.findByCategory("aaa").toString(); }
	 */

	@RequestMapping(value = "/main")
	@ResponseBody
	public ModelAndView hello2() {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("main.html");
		return modelAndView;
//+ productDatabaseHandler.findByCategory("aaa").toString();
	}

	@RequestMapping(value = "/getName")
	@ResponseBody
	public String hello1(@RequestParam String a) {
		return "Nie wiem " + a;
	}
}
