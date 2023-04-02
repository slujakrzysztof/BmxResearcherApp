package com.bmxApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bmxApp.dto.product.CompareProductDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.service.comparison.ComparisonControllerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ComparisonController {

	
	private final ComparisonControllerService comparisonControllerService;
	
	@PostMapping("/compare")
	public String compare(@ModelAttribute("compareProduct") CompareProductDTO product, Model model) {
		
		System.out.println("NAME: " + product.getProductName());
		
		System.out.println("Shop: " + product.getShopName());
		System.out.println("PRICE: " + product.getPrice());
		System.out.println("DESC: " + product.getDescription());
		
		comparisonControllerService.compare(product);
		
		model.addAttribute("productName", product.getProductName());
		model.addAttribute("shopName", product.getShopName());
		model.addAttribute("price", product.getPrice());
		return "comparator";
	}
}
