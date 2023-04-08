package com.bmxApp.formatter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProductFormatter {

	private final static int NUMBERS_AFTER_DOT = 2;
	
	public static BigDecimal format(BigDecimal number) {
		
		return number.setScale(NUMBERS_AFTER_DOT, RoundingMode.HALF_UP);
	}
	
}
