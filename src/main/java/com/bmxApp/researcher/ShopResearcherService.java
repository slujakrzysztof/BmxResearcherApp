package com.bmxApp.researcher;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.helper.ValidationException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.Shop;
import com.bmxApp.exception.NotFoundException;
import com.bmxApp.formatter.ProductFormatter;
import com.bmxApp.formatter.StringFormatter;
import com.bmxApp.manager.PropertyManager;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.product.Product;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.repository.ProductRepository;

@Component
@Scope("prototype")
public class ShopResearcherService {

	final int MAX_TRIES = 20;

	private String html;
	protected Document document;
	private Elements div, productNameElements, productPriceElements, productUrlElements, imageUrlElements;
	private String category;
	private String shopName;


	ArrayList<Product> products = new ArrayList<>();
	ArrayList<Product> specificProducts = new ArrayList<>();
	List<String> pagesArray = new ArrayList<>();
	ArrayList<String> pagesArrayAve = new ArrayList<>();

	List<Product> existingProducts;

	@Autowired
	ProductRepository productRepository;

	BigDecimal price = new BigDecimal(0);
	String productURLComplete;

	public String getHTML() {
		return this.html;
	}

	public void setHTML(String html) {
		this.html = html;
	}

	public Document getDocument() {
		return this.document;
	}

	public void setConnection(String html) {
		int tryCounter = 0;

		while (tryCounter < MAX_TRIES) {
			try {
				document = Jsoup.connect(html).timeout(6000).userAgent(
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36")
						.get();
				break;
			} catch (IOException ex) {
				if (++tryCounter == MAX_TRIES) {
					throw new NullPointerException();
				}
			}
		}
	}

	public String getShopName() {
		return this.shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return this.category;
	}

	public String findPartUrl(String category) {

		String[] propertyNames = { PropertyManager.getInstance().URL_SEARCH_PAGE(),
				PropertyManager.getInstance().URL_SEARCH_FRAMES() };
		String categoryShop = PropertyReader.getInstance().getProperty(category.toLowerCase());
		List<Element> partUrlList = new ArrayList<>();
		Elements partUrls;

		for (int counter = 0; counter < propertyNames.length; counter++) {

			partUrls = this.getDocument().select(propertyNames[counter]);

			partUrlList = partUrls.stream().filter(
					element -> element.absUrl(PropertyManager.getInstance().ABS_URL_ATTRIBUTE()).contains(categoryShop))
					.limit(1).collect(Collectors.toList());

			if (partUrlList.size() > 0)
				break;
			else if (partUrlList.size() <= 0 && counter == 1)
				throw new NotFoundException();
		}

		return partUrlList.get(0).absUrl(PropertyManager.getInstance().ABS_URL_ATTRIBUTE());
	}

	private List<String> findPagesInCategory(String partUrl) {

		List<String> pageUrlList = new ArrayList<String>();

		try {

			String propertyName = PropertyManager.getInstance().PAGE_NUMBER();
			boolean allProductsDisplay = Boolean.valueOf(PropertyManager.getInstance().ALL_PRODUCTS_DISPLAY());
			Elements pageUrlElements = this.getDocument().select(propertyName);

			pageUrlList = pageUrlElements.stream().map(pageUrlElement -> pageUrlElement.attr("href"))
					.map(pageUrlElement -> pageUrlElement = pageUrlElement.replaceAll("\\s", "")).distinct()
					.collect(Collectors.toList());

			long listSize = pageUrlList.size();

			if (allProductsDisplay) {
				pageUrlList = pageUrlList.stream().skip(listSize - 1).collect(Collectors.toList());
			} else if (pageUrlList.isEmpty())
				pageUrlList.add(partUrl);
			else if (pageUrlList.get(0).isBlank()) {
				pageUrlList.set(0, partUrl);
			}

		} catch (ValidationException ex) {
			pageUrlList.add(partUrl);
		}

		return pageUrlList;
	}

	public void getProductsFromPage(String shopName) {

		div = this.getDocument().select(PropertyManager.getInstance().DIV());// PropertyReader.getInstance().getProperty("div"));
		productNameElements = div.select(PropertyManager.getInstance().PRODUCT_NAME());
		productPriceElements = div.select(PropertyManager.getInstance().PRODUCT_PRICE());
		productUrlElements = div.select(PropertyManager.getInstance().PRODUCT_URL());
		// DLA MANYFEST DOC DLA INNYCH DIV
		if (shopName.equals(Shop.MANYFESTBMX.name().toLowerCase()))
			imageUrlElements = this.getDocument().select(PropertyManager.getInstance().IMAGE_URL());
		else
			imageUrlElements = div.select(PropertyManager.getInstance().IMAGE_URL());

	}

	private void formatDataStructure(String shopName, int i) {

		double priceText;
		
		if (shopName.equalsIgnoreCase(Shop.MANYFESTBMX.name())) {
			priceText = Double.parseDouble(productPriceElements.get(i).attr("content"));
		} else {
			try {
				priceText = Double.parseDouble(productPriceElements.get(i).text().replaceAll("[^\\d.]", "")); //Double.parseDouble(productPriceElements.get(i).text().replaceAll("[^\\d.]", ""));
			} catch (NumberFormatException ex) {
				priceText = Double.parseDouble(productPriceElements.get(i)
							.select(PropertyManager.getInstance().PRODUCT_PRICE_DISCOUNT())
							.text()
							.replaceAll("[^\\d.]", ""));
						
						/*Double.parseDouble(
						productPriceElements.get(i).select(PropertyManager.getInstance().PRODUCT_PRICE_DISCOUNT())
								.text().replaceAll("[^\\d.]", ""));*/
				// PropertyReader.getInstance().getProperty("productDiscountPriceElement")).text()
			}
		}
		
		price = ProductFormatter.format(new BigDecimal(priceText));

		if (shopName.equalsIgnoreCase(Shop.AVEBMX.name())) {
			productURLComplete = "https://avebmx.pl"
					+ productUrlElements.get(i).attr(PropertyManager.getInstance().URL_ATTRIBUTE());
		} else {
			productURLComplete = productUrlElements.get(i).attr(PropertyManager.getInstance().URL_ATTRIBUTE());
		}

	}

	public LinkedList<ProductDTO> getFormattedDataProducts(String shopName, String category) {

		LinkedList<ProductDTO> productList = new LinkedList<>();

		for (int i = 0; i < productNameElements.size(); i++) {
			this.formatDataStructure(shopName, i);
			
			String productName = productNameElements.get(i).text().replace("'", "");

			productList.add(ProductDTO.builder().productName(productName).shopName(shopName).category(category)
					.price(price).url(productURLComplete)
					.imageUrl(imageUrlElements.get(i).attr(PropertyManager.getInstance().IMAGE_ATTRIBUTE())).build());
		}

		return productList;
	}

	public void searchNewProducts(String shopName, String category, String url) {

		List<String> pagesList = this.findPagesInCategory(url);
		List<Product> productsList = new LinkedList<>();
		ProductMapper productMapper = new ProductMapper();

		pagesList.stream().forEach(page -> {
			this.setConnection(page);
			this.getProductsFromPage(shopName);
			this.getFormattedDataProducts(shopName, category)
					.forEach(productDto -> productsList.add(productMapper.apply(productDto)));
		});

		productRepository.saveAll((Iterable<Product>) productsList);
	}

	public String getDescription(String uri) {

		Elements desc;
		
		this.setConnection(uri);
		Document doc = getDocument();

		Elements page = doc.select(PropertyManager.getInstance().DESCRIPTION());
		
		if(page.isEmpty()) 
			return "NULL";

		Elements paragraphs = page.select("p, li, span");

		desc = (paragraphs.isEmpty() ? page : paragraphs);
		
		return desc.html().replace("&nbsp;", " ");

	}
	
	public String getCompareDescription(String uri) {
		
		return StringFormatter.formatCompareDescription(getDescription(uri));
	}


}