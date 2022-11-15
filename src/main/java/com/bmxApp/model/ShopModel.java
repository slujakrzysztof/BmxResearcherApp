package com.bmxApp.model;

import com.bmxApp.enums.Shop;

public class ShopModel {

	private Shop shop;
	private String partName;

	public ShopModel() {
		// TODO Auto-generated constructor stub
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	@Override
	public String toString() {
		return "ShopModel [shop=" + shop + "]";
	}

}
