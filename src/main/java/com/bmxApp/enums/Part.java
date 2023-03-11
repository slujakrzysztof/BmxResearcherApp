package com.bmxApp.enums;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.hibernate.internal.build.AllowSysOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Constants;

import com.bmxApp.service.main.MainControllerService;

public enum Part {
	GRIPS("Gripy"), BARS("Kierownice"), BARENDS("Barendy"), STEMS("Wsporniki"), HEADS("Stery"), FRAMES("Ramy"),
	FORKS("Widelce"), RIMS("Obrecze"), TIRES("Opony"), SPOKES("Szprychy"), HUBS("Piasty"), POSTS("Sztyce"),
	GEARS("Zebatki"), CRANKS("Korby"), PEDALS("Pedaly"), CHAINS("Lancuchy"), SEATS("Siodelka"), SUPPORTS("Suporty"),
	PEGS("Pegi");

	@Autowired
	MainControllerService mainControllerService;

	private String PATH = "properties/" + "polish" + ".properties";
	private static Logger logger = LoggerFactory.getLogger(Constants.class);
	private static Properties properties;
	private String value;

	private Part(String value) {
		this.value = value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private void setSpecificValue(String shopName) {
		if(shopName.equals(Shop.MANYFESTBMX.name())) {
			STEMS.setValue("mosty");
		}
	}

	public String getValue(String shopName) {
		this.setSpecificValue(shopName);
		return value;
	}

	public String getValue() {
		return this.value;
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

	public static Part fromStringValue(String value) {
		for (Part part : Part.values()) {
			if (part.getValue().equalsIgnoreCase(value)) {
				return part;
			}
		}
		return null;
	}
	
	public static Part fromString(String partName) {
		for (Part part : Part.values()) {
			if (part.name().equalsIgnoreCase(partName)) {
				return part;
			}
		}
		return null;
	}
	
	

	/*
	 * public String getValue() { if (value == null) { init(); } return value; }
	 */
}
