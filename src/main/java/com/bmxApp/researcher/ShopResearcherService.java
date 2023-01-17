package com.bmxApp.researcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.helper.ValidationException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bmxApp.dto.discount.DiscountDTO;
import com.bmxApp.dto.product.ProductDTO;
import com.bmxApp.enums.Shop;
import com.bmxApp.exception.NotFoundException;
import com.bmxApp.manager.PropertyManager;
import com.bmxApp.mapper.product.ProductMapper;
import com.bmxApp.model.Product;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.repository.ProductRepository;

@Component
@Scope("prototype")
public class ShopResearcherService {

	final int MAX_TRIES = 20;
	private String html;
	protected Document document;
	private int tryCounter = 0;
	private Elements div, productNameElements, productPriceElements, productUrlElements, imageUrlElements;
	private String category;
	private String shopName;

	private DiscountDTO discount = new DiscountDTO();

	Document.OutputSettings outputSettings = new Document.OutputSettings();

	ArrayList<Product> products = new ArrayList<>();
	ArrayList<Product> specificProducts = new ArrayList<>();
	List<String> pagesArray = new ArrayList<>();
	ArrayList<String> pagesArrayAve = new ArrayList<>();

	List<Product> existingProducts;

	@Autowired
	ProductRepository productRepository;

	double price = 0;
	String productURLComplete;


	public DiscountDTO getDiscount() {
		return this.discount;
	}

	public void setDiscount(DiscountDTO discount) {
		this.discount = discount;
	}

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

		String propertyName = PropertyManager.getInstance().URL_SEARCH_PAGE();
		Elements partUrls = this.getDocument().select(propertyName);
		String categoryShop = PropertyReader.getInstance().getProperty(category.toLowerCase());

		List<Element> partUrlList = partUrls.stream().filter(
				element -> element.absUrl(PropertyManager.getInstance().ABS_URL_ATTRIBUTE()).contains(categoryShop))
				.limit(1).collect(Collectors.toList());

		if (partUrlList.size() == 0)
			throw new NotFoundException();

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
			
			System.out.println("PAGE URL LIST : " + pageUrlList);
			
			if (allProductsDisplay) {
				System.out.println("HAHAH: " + pageUrlList.stream().skip(listSize - 1).collect(Collectors.toList()));
				pageUrlList = pageUrlList.stream().skip(listSize - 1).collect(Collectors.toList());
			}
			else if (pageUrlList.isEmpty())
				pageUrlList.add(partUrl);
			else if(pageUrlList.get(0).isBlank()) {
				System.out.println("JESTEM TU" + partUrl);
				pageUrlList.set(0, partUrl);
				System.out.println("PAGE URL LIST 2: " + pageUrlList);			}
				

		} catch (ValidationException ex) {
			pageUrlList.add(partUrl);
		}

		return pageUrlList;
	}

	private void getProductsFromPage(String shopName) {

		div = this.getDocument().select(PropertyManager.getInstance().DIV());// PropertyReader.getInstance().getProperty("div"));
		productNameElements = div.select(PropertyManager.getInstance().PRODUCT_NAME());
		productPriceElements = div.select(PropertyManager.getInstance().PRODUCT_PRICE());
		productUrlElements = div.select(PropertyManager.getInstance().PRODUCT_URL());
		// DLA MANYFEST DOC DLA INNYCH DIV
		if (shopName.equals(Shop.MANYFESTBMX.name().toLowerCase()))
			imageUrlElements = this.getDocument().select(PropertyManager.getInstance().IMAGE_URL());
		else
			imageUrlElements = div.select(PropertyManager.getInstance().IMAGE_URL());
		
		System.out.println("DIIIIIIIV: "  + div);
		System.out.println("NAAAAAAAAMES : " + productNameElements);
		System.out.println("PRIIIIIICES : " + productPriceElements);
		System.out.println("URRRRRRRRL : " + productUrlElements);
		System.out.println("IMG URRRRRRRRL : " + imageUrlElements);
		
	}
	
	private void formatDataStructure(String shopName, int i) {

		if (shopName.equalsIgnoreCase(Shop.MANYFESTBMX.name())) {
			price = Double.parseDouble(productPriceElements.get(i).attr("content"));
		} else {
			try {
				price = Double.parseDouble(productPriceElements.get(i).text().replaceAll("[^\\d.]", ""));
			} catch (NumberFormatException ex) {
				price = Double.parseDouble(productPriceElements.get(i)
						.select(PropertyManager.getInstance().PRODUCT_PRICE_DISCOUNT()).text().replaceAll("[^\\d.]", ""));
				// PropertyReader.getInstance().getProperty("productDiscountPriceElement")).text()
			}
		}

		if (shopName.equalsIgnoreCase(Shop.AVEBMX.name())) {
			productURLComplete = "https://avebmx.pl"
					+ productUrlElements.get(i).attr(PropertyManager.getInstance().URL_ATTRIBUTE());
			// PropertyReader.getInstance().getProperty("urlAtrribute"));
		} else {
			productURLComplete = productUrlElements.get(i).attr(PropertyManager.getInstance().URL_ATTRIBUTE());
			// PropertyReader.getInstance().getProperty("urlAtrribute"));
		}

	}

	private LinkedList<ProductDTO> getFormattedDataProducts(String shopName, String category) {

		LinkedList<ProductDTO> productList = new LinkedList<>();

		for (int i = 0; i < productNameElements.size(); i++) {
			this.formatDataStructure(shopName, i);

			String productName = productNameElements.get(i).text().replace("'", "");
			
			productList.add(ProductDTO.builder()
					  .productName(productName)
					  .shopName(shopName)
					  .category(category)
					  .price(price)
					  .url(productURLComplete)
					  .imageUrl(imageUrlElements.get(i).attr(PropertyManager.getInstance().IMAGE_ATTRIBUTE()))
					  .build());
		}

		return productList;
	}

	public void searchNewProducts(String shopName, String category, String url) {

		ArrayList<String> pagesList = (ArrayList<String>) this.findPagesInCategory(url);
		LinkedList<Product> productsList = new LinkedList<>();
		System.out.println("SEARCH CATEGORY : " + category);
		
		pagesList.stream().forEach(page -> {
			this.setConnection(page);
			this.getProductsFromPage(shopName);
			this.getFormattedDataProducts(shopName, category)
					.forEach(dtoProduct -> productsList.add(ProductMapper.mapToProduct(dtoProduct)));
		});		
		
		productRepository.saveAll((Iterable<Product>) productsList);
	}

	public String getDescription(String className) throws NullPointerException {
		String[] separator = className.split(",");
		String finalS, str;
		Document jsoupDoc;
		for (int i = 0; i < separator.length; i++) {
			finalS = this.getDocument().select(separator[i]).html();
			jsoupDoc = Jsoup.parse(finalS);
			// select all <br> tags and append \n after that
			jsoupDoc.select("br").after("\\n");
			// select all <p> tags and prepend \n before that
			jsoupDoc.select("p").before("\\n");
			// get the HTML from the document, and retaining original new lines
			System.out.println("SDHIUASHD: " + Jsoup.clean(jsoupDoc.html(), "", Safelist.none(), outputSettings));
			str = jsoupDoc.html().replaceAll("\\\\n", "\n").replaceAll("&nbsp;", "");
			outputSettings.prettyPrint(false);
			if (!Jsoup.clean(str, "", Safelist.none(), outputSettings).trim().isEmpty())
				return Jsoup.clean(str, "", Safelist.none(), outputSettings);
		}
		throw new NullPointerException();
	}
	
}