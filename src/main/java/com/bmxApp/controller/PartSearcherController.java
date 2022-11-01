package com.bmxApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.bmxApp.service.PartSearcherControllerService;

@RestController
public class PartSearcherController {

	@Autowired
	PartSearcherControllerService partSearcherControllerService;
}
