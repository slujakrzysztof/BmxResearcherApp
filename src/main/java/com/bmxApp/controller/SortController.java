package com.bmxApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.search.SearchService;
import com.bmxApp.service.sort.SortService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SortController {

	private final SortService sortService;
	
	@GetMapping(value = "/sortFilter")
	public ModelAndView sortFilter(Model model, @RequestParam("currentUrl") String currentUrl, @RequestParam("sortedBy") String sortedBy) {
		
		return new ModelAndView("redirect:" + sortService.getSortUrl(currentUrl, sortedBy));

	}
	
}
