package com.bmxApp.model;

import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "BasketTable")
public class BasketProduct extends Product {

	public BasketProduct(String productName, String shopName, String category, double price) {
		super(productName, shopName, category, price);
	}

	public BasketProduct() {
	}

	@Column(name = "addingDate")
	private Calendar addingDate;

}
