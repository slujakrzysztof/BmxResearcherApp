package com.bmxApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.bmxApp.creator.PathCreator;
import com.bmxApp.dto.product.CompareProductDTO;
import com.bmxApp.service.comparison.ComparisonService;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ComparisonController {

	private final ComparisonService comparisonControllerService;

	@PostMapping(value = "/compare")
	public RedirectView compare(@Nullable @ModelAttribute("compareProduct") CompareProductDTO product,
			@RequestParam("currentUrl") String currentUrl) {

		comparisonControllerService.compare(product);

		return new RedirectView(currentUrl);
	}

	@GetMapping(value = "/comparator")
	public String showComparator(Model model, HttpServletRequest request) {

		model.addAttribute("compareProduct", comparisonControllerService.getCompareProducts());
		model.addAttribute("currentUrl", PathCreator.getCurrentUrl(request));

		return "comparator";
	}

	@DeleteMapping("/deleteCompareProduct/{id}")
	public RedirectView deleteFromComparator(@PathVariable("id") int id,
			@RequestParam("currentUrl") String currentUrl) {

		comparisonControllerService.deleteProduct(id);

		return new RedirectView(currentUrl);
	}
}
