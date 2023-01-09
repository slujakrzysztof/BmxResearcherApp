package com.bmxApp.dto.shopModel;

import com.bmxApp.enums.Shop;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShopModelDTO {

	private Shop shop;
	private String partName;

}
