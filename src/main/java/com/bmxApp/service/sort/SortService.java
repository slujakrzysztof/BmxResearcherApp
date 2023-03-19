package com.bmxApp.service.sort;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.SortingItem;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@Getter
@Setter
public class SortService {

	private final String NAME = SortingItem.NAME.name();
	private final String PRICE = SortingItem.PRICE.name();
	private final String SHOP = SortingItem.SHOP.name();

	@Value("false")
	private boolean sortedBy;

	public List<ProductDTO> sortProductDTO(String sortedBy, List<ProductDTO> products) {

		Comparator<ProductDTO> comparator;
		List<ProductDTO> sortedProducts;

		if (sortedBy.equalsIgnoreCase(NAME))
			comparator = Comparator.comparing(ProductDTO::getProductName);
		else if (sortedBy.equalsIgnoreCase(PRICE))
			comparator = Comparator.comparing(ProductDTO::getPrice);
		else // if(sortedBy.equalsIgnoreCase(SortingItem.SHOP.name()))
			comparator = Comparator.comparing(ProductDTO::getShopName);

		sortedProducts = products.stream().sorted(comparator).collect(Collectors.toList());

		return sortedProducts;
	}

	public String getSortUrl(String currentUrl, String sortValue) {

		StringBuilder url = new StringBuilder();

		if (currentUrl.contains("sortedBy")) {
			if (currentUrl.indexOf("&", currentUrl.indexOf("sortedBy")) <= 0)
				currentUrl = currentUrl
						.replace(currentUrl.substring(currentUrl.indexOf("sortedBy") - 1, currentUrl.length()), "");

			else
				currentUrl = currentUrl.replace(currentUrl.substring(currentUrl.indexOf("sortedBy") - 1,
						currentUrl.indexOf("&", currentUrl.indexOf("sortedBy")) + 1), "");

		}

		url.append(currentUrl).append("&").append("sortedBy=").append(sortValue);

		return url.toString();
	}

}
