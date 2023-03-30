package com.bmxApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.bmxApp.creator.PathCreator;
import com.bmxApp.service.cart.ShoppingCartService;
import com.bmxApp.service.search.SearchService;
import com.bmxApp.service.sort.SortService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SortController {


	@GetMapping(value = "/sort")
	public RedirectView sort(Model model, @RequestParam("currentUrl") String currentUrl, @RequestParam("sortedBy") String sortedBy) {
		
		System.out.println("CURRENT: " + currentUrl);
		
		return new RedirectView(PathCreator.createSortUrl(currentUrl, sortedBy));
	}
	
}
