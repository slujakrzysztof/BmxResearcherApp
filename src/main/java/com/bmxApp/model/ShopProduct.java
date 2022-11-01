package com.bmxApp.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "ShopProductTable")
public class ShopProduct extends Product {

	@Column(name = "url", length = 255)
	private String url;
	@Column(name = "imageUrl", length = 255)
	private String imageUrl;

	public ShopProduct() {

	}

	public ShopProduct(String productName, String shopName, String category, String url, String imageUrl,
			double price) {
		super(productName, shopName, category, price);
		this.url = url;
		this.imageUrl = imageUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "ShopProduct [url=" + url + ", imageUrl=" + imageUrl + ", getId()=" + getId() + ", getProductName()="
				+ getProductName() + ", getShopName()=" + getShopName() + ", getCategory()=" + getCategory()
				+ ", getPrice()=" + getPrice() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}
