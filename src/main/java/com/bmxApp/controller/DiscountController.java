package com.bmxApp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import com.bmxApp.service.discount.DiscountService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DiscountController {

	private final DiscountService discountService;

	@GetMapping(value = "/applyDiscount")
	public ModelAndView applyDiscount(@RequestParam("currentUrl") String currentUrl,
			@RequestParam("discountValue") String discountValue, Model model, HttpServletRequest request) {

		discountService.setDiscount(Integer.parseInt(discountValue));
		
		return new ModelAndView("redirect:" + discountService.createUrlWithDiscount(currentUrl, discountValue));
		
	}
	
	@GetMapping(value = "/resetDiscount")
	public ModelAndView resetDiscount(@RequestParam("currentUrl") String currentUrl, Model model, HttpServletRequest request) {

		discountService.resetDiscount();
		
		return new ModelAndView("redirect:" + discountService.createUrlWithDiscount(currentUrl, "0"));
		
	}
}
