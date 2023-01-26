package com.bmxApp.dto.discount;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class DiscountDTO {
	
	@Value("0")
	int value;
	boolean applied;

}
