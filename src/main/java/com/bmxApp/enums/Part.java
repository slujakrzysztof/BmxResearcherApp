package com.bmxApp.enums;



import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Constants;

import com.bmxApp.service.MainControllerService;



public enum Part {
	GRIPS,
	BARS,
	BARENDS,
	STEMS,
	HEADS,
	FRAMES,
	FORKS,
	RIMS,
	TIRES,
	SPOKES,
	HUBS,
	POSTS,
	GEARS,
	CRANKS,
	PEDALS,
	CHAINS,
	SEATS,
	SUPPORTS;
	
	@Autowired
	MainControllerService mainControllerService;
	
	private String PATH = mainControllerService.getLanguage();
	private static Logger logger = LoggerFactory.getLogger(Constants.class);
	private static Properties properties;
}
