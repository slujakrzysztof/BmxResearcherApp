package com.bmxApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.bmxApp.creator.PathCreator;
import com.bmxApp.dto.product.CompareProductDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.service.comparison.ComparisonService;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ComparisonController {

	private final ComparisonService comparisonControllerService;

	@RequestMapping(value = "/compare", method = { RequestMethod.POST, RequestMethod.GET })
	public String compare(@Nullable @ModelAttribute("compareProduct") CompareProductDTO product, Model model,
			HttpServletRequest request) {

		System.out.println("PROD: " + product.toString());
		
		if(product != null) comparisonControllerService.compare(product);
		
		model.addAttribute("compareProduct", comparisonControllerService.getCompareProducts());
		model.addAttribute("currentUrl", PathCreator.getCurrentUrl(request));
		return "comparator";
	}

	@DeleteMapping("/deleteCompareProduct/{id}")
	public RedirectView deleteFromComparator(@PathVariable("id") int id, @RequestParam("currentUrl") String currentUrl,
			Model model) {

		comparisonControllerService.deleteProduct(id);

		model.addAttribute("compareProduct", comparisonControllerService.getCompareProducts());
		return new RedirectView(currentUrl);
	}
}
