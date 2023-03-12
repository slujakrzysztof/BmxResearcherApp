package com.bmxApp.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.service.sort.SortService;

import ch.qos.logback.core.model.Model;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Controller
public class SortController {

	private final SortService sortService;
	
	@GetMapping("/sort")
	public String sortProducts(Model model, @RequestParam("products") List<ProductDTO> products) {
		
		System.out.println("PROD: " + products);
		
		return null;
	}
}
