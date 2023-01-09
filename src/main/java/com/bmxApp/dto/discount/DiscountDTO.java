package com.bmxApp.dto.discount;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Data
public class DiscountDTO {
	
	@Value("0")
	int value;
	boolean applied;

}
