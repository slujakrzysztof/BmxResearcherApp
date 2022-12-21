package com.bmxApp.model;

import org.springframework.beans.factory.annotation.Value;

public class Discount {
	
	@Value("0")
	public int discountValue;

	public int getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(int discountValue) {
		this.discountValue = discountValue;
	}
}
