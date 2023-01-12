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
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "ProductTable")
@Data
@Builder
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

}
