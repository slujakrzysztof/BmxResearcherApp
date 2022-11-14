package com.bmxApp.model;

import com.bmxApp.enums.Shop;

public class ShopModel {

	private Shop shop;

	public ShopModel() {
		// TODO Auto-generated constructor stub
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	@Override
	public String toString() {
		return "ShopModel [shop=" + shop + "]";
	}

}
