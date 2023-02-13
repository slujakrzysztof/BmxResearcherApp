package com.bmxApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bmxApp.dto.shopModel.ShopModelDTO;
import com.bmxApp.service.main.MainControllerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping({ "/", "/main" })
@RequiredArgsConstructor
public class MainController {

	private final MainControllerService mainControllerService;
	
	@GetMapping("/main")
	public String showMainPage(Model model) {
				
		model.addAttribute("shopModel", new ShopModelDTO());
		model.addAttribute("basketProducts", mainControllerService.getBasketProducts());
		return "main";
	}

}
