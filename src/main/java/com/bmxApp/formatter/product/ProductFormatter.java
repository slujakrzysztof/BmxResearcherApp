package com.bmxApp.formatter.product;

import java.util.Locale;

public class ProductFormatter {

	public static String formatProductPrice(double price) {
		return String.format(Locale.US, "%.2f", price);
	}
}
