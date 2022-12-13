package com.bmxApp.model;

import java.util.Locale;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "ProductTable")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "productName", length = 255)
	private String productName;
	@Column(name = "shopName", length = 255)
	private String shopName;
	@Column(name = "category", length = 255)
	private String category;
	@Column(name = "price", length = 255)
	private double price;
	@Column(name = "url", length = 255)
	private String url;
	@Column(name = "imageUrl", length = 255)
	private String imageUrl;

	public Product(String productName, String shopName, String category, String url, String imageUrl, double price) {
		this.productName = productName;
		this.shopName = shopName;
		this.category = category;
		this.price = price;
		this.url = url;
		this.imageUrl = imageUrl;
	}

	@Transient
	public String getImage() {
		return "/images/image";
	}

	public Product() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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

	public String formatPrice() {
		return String.format(Locale.US, "%.2f", this.getPrice());
	}
	
	@Override
	public String toString() {
		return "Product [id=" + id + ", productName=" + productName + ", shopName=" + shopName + ", category="
				+ category + ", price=" + price + ", url=" + url + ", imageUrl=" + imageUrl + "]";
	}
}
