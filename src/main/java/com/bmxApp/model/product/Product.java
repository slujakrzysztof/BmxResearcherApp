package com.bmxApp.model.product;


import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ProductTable")
@Setter
@Getter
@NoArgsConstructor
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
	private BigDecimal price;
	@Column(name = "url", length = 255)
	private String url;
	@Column(name = "imageUrl", length = 255)
	private String imageUrl;

	public Product(String productName, 
				   String shopName, 
				   String category, 
				   BigDecimal price, 
				   String url, 
				   String imageUrl) {
		
		this.productName = productName;
		this.shopName = shopName;
		this.category = category;
		this.price = price;
		this.url = url;
		this.imageUrl = imageUrl;
	}
}
