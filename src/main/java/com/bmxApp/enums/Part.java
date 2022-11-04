package com.bmxApp.enums;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Constants;

import com.bmxApp.service.MainControllerService;

public enum Part {
	GRIPS("gripy"), BARS("kierownice"), BARENDS("barendy"), STEMS("mostki"), HEADS("stery"), FRAMES("ramy"),
	FORKS("widelce"), RIMS("obrecze"), TIRES("opony"), SPOKES("szprychy"), HUBS("piasty"), POSTS("sztyce"),
	GEARS("zebatki"), CRANKS("korby"), PEDALS("pedaly"), CHAINS("lancuchy"), SEATS("siodelka"), SUPPORTS("suporty");

	@Autowired
	MainControllerService mainControllerService;

	private String PATH = "properties/" + "polish" + ".properties";
	private static Logger logger = LoggerFactory.getLogger(Constants.class);
	private static Properties properties;
	private String value;

	private Part() {
		// TODO Auto-generated constructor stub
	}

	private Part(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	private void init() {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(Constants.class.getResourceAsStream(PATH));
			} catch (Exception e) {
				logger.error("Unable to load " + PATH + " file from classpath.", e);
				System.exit(1);
			}
		}
		value = (String) properties.get(this.toString());
	}

	/*
	 * public String getValue() { if (value == null) { init(); } return value; }
	 */
}
