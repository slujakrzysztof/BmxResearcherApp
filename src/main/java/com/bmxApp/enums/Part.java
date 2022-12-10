package com.bmxApp.enums;

import java.util.Properties;

import org.hibernate.internal.build.AllowSysOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Constants;

import com.bmxApp.service.MainControllerService;

public enum Part {
	GRIPS("gripy"), BARS("kierownice"), BARENDS("barendy"), STEMS("mostki"), HEADS("stery"), FRAMES("ramy"),
	FORKS("widelce"), RIMS("obrecze"), TIRES("opony"), SPOKES("szprychy"), HUBS("piasty"), POSTS("sztyce"),
	GEARS("zebatki"), CRANKS("korby"), PEDALS("pedaly"), CHAINS("lancuchy"), SEATS("siodelka"), SUPPORTS("suporty"), PEGS("pegi");

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
	
	public void setValue(String value) {
		this.value = value;
	}

	public String getValue(String shopName) {
		if(shopName.equals(Shop.AVEBMX.name())) {
			System.out.println("AVEEEEEEEEEEEEEEEEEE");
			STEMS.setValue("wsporniki-kierownicy");
			System.out.println("MOSTKI VALUE: " + STEMS.getValue());
		}
		return value;
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
	
    public static Part fromString(String text) {
        for (Part pt : Part.values()) {
            if (pt.getValue().equalsIgnoreCase(text)) {
                return pt;
            }
        }
        return null;
    }
	/*
	 * public String getValue() { if (value == null) { init(); } return value; }
	 */
}
