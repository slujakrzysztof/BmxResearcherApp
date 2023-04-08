package com.bmxApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bmxApp.dto.product.CompareProductDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.service.comparison.ComparisonService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ComparisonController {

	
	private final ComparisonService comparisonControllerService;
	
	@PostMapping("/compare")
	public String compare(@ModelAttribute("compareProduct") CompareProductDTO product, Model model) {
		
		model.addAttribute("compareProduct", comparisonControllerService.compare(product));
		return "comparator";
	}
}
