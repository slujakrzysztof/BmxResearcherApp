package com.bmxApp.model;

import org.springframework.stereotype.Component;

import com.bmxApp.enums.Shop;

@Component
public class Test {

	private String shop;

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	@Override
	public String toString() {
		return "Test [shop=" + shop + "]";
	}


}
