package com.bmxApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bmxApp.service.comparison.ComparisonControllerService;

@RestController
public class ComparisonController {

	@Autowired
	ComparisonControllerService comparisonControllerService;
}
