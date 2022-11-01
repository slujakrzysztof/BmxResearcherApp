package com.bmxApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bmxApp.service.PartSearcherControllerService;

@RestController
@RequestMapping(name = "/searchedParts")
public class PartSearcherController {

	@Autowired
	PartSearcherControllerService partSearcherControllerService;

	@GetMapping
	public ModelAndView getSearchedProducts(@RequestParam String shopName, @RequestParam String category) {
		ModelAndView model = new ModelAndView("products.html");
		model.addObject("products", partSearcherControllerService.getProductsByCategoryAndShopName(category, shopName));
		return model;
	}
}
