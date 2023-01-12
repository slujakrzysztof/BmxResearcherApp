package com.bmxApp.researcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
	private int numberOfPages;
	private String html;
	protected Document document;
	private int tryCounter = 0;
	private boolean initialized;
	private Elements div, productNameElements, productPriceElements, productUrlElements, imageUrlElements, pages;
	private int productIndex = 0, indexSearchPage = 0;
	private String category;
	private String categoryEnum;
	private String shopName;
	private boolean partFound = false;

	private DiscountDTO discount = new DiscountDTO();

	Document.OutputSettings outputSettings = new Document.OutputSettings();

	ArrayList<com.bmxApp.model.Product> products = new ArrayList<>();
	ArrayList<Product> specificProducts = new ArrayList<>();
	List<String> pagesArray = new ArrayList<>();
	ArrayList<String> pagesArrayAve = new ArrayList<>();

	List<Product> existingProducts;

	@Autowired
	ProductRepository productRepository;

	double price = 0;
	String productURLComplete;

	/*
	 * public ShopResearcher(String html, String shopName) { this.html = html;
	 * this.shopName = shopName; }
	 */

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

	public void connectToShopResearcher(String html) {

	}

	/*
	 * public void startSearching(String html) { this.setHTML(html);
	 * this.setConnection(); System.out.println("AAA: " +
	 * PropertyManager.getInstance().URL_SEARCH_PAGE);
	 * this.findPageUrl(this.getDocument().select(PropertyManager.getInstance().
	 * URL_SEARCH_PAGE), true);
	 * //this.getDocument().select(PropertyReader.getInstance().getProperty(
	 * "urlSearch")), true); }
	 */

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

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public String findPartUrl(String category) {

		String propertyName = PropertyManager.getInstance().URL_SEARCH_PAGE;
		Elements partUrls = this.getDocument().select(propertyName);

		List<String> partUrlList = partUrls.stream().filter(element -> element.absUrl("href").contains(category))
				.limit(1).map(Object::toString).collect(Collectors.toList());

		if (partUrlList.size() == 0)
			throw new NotFoundException();

		return partUrlList.toString();
	}

	/*
	 * public void searchPage() { Elements partPage =
	 * this.getDocument().select(PropertyReader.getInstance().getProperty(
	 * "urlSearch")); while (!partFound) { if (findProductPage(partPage)) return;
	 * partPage =
	 * this.getDocument().select(PropertyReader.getInstance().getProperty(
	 * "urlSearchFrames")); } }
	 */

	private List<String> findPagesInSpecificCategory(String partUrl) {

		String propertyName = PropertyManager.getInstance().PAGE_NUMBER;
		boolean allProductsDisplay = Boolean.valueOf(PropertyManager.getInstance().ALL_PRODUCTS_DISPLAY);
		Elements pageUrlElements = this.getDocument().select(propertyName);
		long listSize = pageUrlElements.size();

		List<String> pageUrlList = pageUrlElements.stream().map(pageUrlElement -> pageUrlElement.attr("href"))
				.distinct().collect(Collectors.toList());
		;

		if (allProductsDisplay)
			return pageUrlList.stream().skip(listSize - 1).collect(Collectors.toList());
		else if (pageUrlList.isEmpty())
			pageUrlList.add(partUrl);

		return pageUrlList;
	}

	// --- Get url to pages with products ---
	public void setPagesArray() {
		pagesArray.clear();
		try {
			pages = this.getDocument().select(PropertyReader.getInstance().getProperty("numberOfPages"));
			int pageNumber = 0;

			if (Boolean.valueOf(PropertyReader.getInstance().getProperty("allProductsDisplay"))) {
				pagesArray.add(pages.get(pages.size() - 1).attr("href"));
			} else {
				for (Element page : pages) {
					System.out.println("STROONY 111: " + page);
					System.out.println("STRONY: " + pages.attr("href"));
					try {
						pageNumber = Integer.parseInt(page.text());
					} catch (NumberFormatException ex) {
						continue;
					}
					pagesArray.add(pages.attr("href").substring(0, pages.attr("href").length() - 1) + pageNumber);
				}
				pagesArray = pagesArray.stream().distinct().collect(Collectors.toList());
			}

		} catch (ValidationException validationException) {
			pagesArray.add(this.html);
		}
		numberOfPages = pagesArray.size();
		if (numberOfPages == 0) {
			pagesArray.add(this.html);
			numberOfPages = pagesArray.size();
		}
		productIndex = 0;
	}

	public List<String> getPagesArray() {
		return pagesArray;
	}

	public String getCategoryEnum() {
		return categoryEnum;
	}

	public void setCategoryEnum(String categoryEnum) {
		this.categoryEnum = categoryEnum;
	}

	private void getProductsFromPage() {

		div = this.getDocument().select(PropertyManager.getInstance().DIV);// PropertyReader.getInstance().getProperty("div"));
		productNameElements = div.select(PropertyManager.getInstance().PRODUCT_NAME);
		productPriceElements = div.select(PropertyManager.getInstance().PRODUCT_PRICE);
		productUrlElements = div.select(PropertyManager.getInstance().PRODUCT_URL);
		// DLA MANYFEST DOC DLA INNYCH DIV
		if (this.getShopName().equals(com.bmxApp.enums.Shop.MANYFESTBMX.name().toLowerCase()))
			imageUrlElements = this.getDocument().select(PropertyManager.getInstance().IMAGE_URL);
		else
			imageUrlElements = div.select(PropertyManager.getInstance().IMAGE_URL);

		/*
		 * div =
		 * this.getDocument().select(PropertyReader.getInstance().getProperty("div"));
		 * productNameElements =
		 * div.select(PropertyReader.getInstance().getProperty("productNameElement"));
		 * productPriceElements =
		 * div.select(PropertyReader.getInstance().getProperty("productPriceElement"));
		 * productUrlElements =
		 * div.select(PropertyReader.getInstance().getProperty("productURLElement")); //
		 * DLA MANYFEST DOC DLA INNYCH DIV if
		 * (this.getShopName().equals(com.bmxApp.enums.Shop.MANYFESTBMX.name().
		 * toLowerCase())) imageUrlElements =
		 * this.getDocument().select(PropertyReader.getInstance().getProperty(
		 * "imageURLElement")); else imageUrlElements =
		 * div.select(PropertyReader.getInstance().getProperty("imageURLElement"));
		 */
	}

	/*
	 * private void searchNextPage() { if (numberOfPages > 1) {
	 * this.startSearching(pagesArray.get(indexSearchPage)); if (indexSearchPage ==
	 * pagesArray.size() - 1) indexSearchPage = pagesArray.size() - 1; else
	 * indexSearchPage++; } }
	 */

	private void formatDataStructure(int i) {

		if (this.getShopName().equals(com.bmxApp.enums.Shop.MANYFESTBMX.name().toLowerCase())) {
			price = Double.parseDouble(productPriceElements.get(i).attr("content"));
		} else {
			try {
				price = Double.parseDouble(productPriceElements.get(i).text().replaceAll("[^\\d.]", ""));
			} catch (NumberFormatException ex) {
				price = Double.parseDouble(productPriceElements.get(i)
						.select(PropertyReader.getInstance().getProperty("productDiscountPriceElement")).text()
						.replaceAll("[^\\d.]", ""));
			}
		}
		if (this.getShopName().equals(com.bmxApp.enums.Shop.AVEBMX.name().toLowerCase())) {
			productURLComplete = "https://avebmx.pl" + productUrlElements.get(i)
					.attr(PropertyReader.getInstance().getProperty("urlAtrribute"));
		} else {
			productURLComplete = productUrlElements.get(i)
					.attr(PropertyReader.getInstance().getProperty("urlAtrribute"));
		}

	}

	private LinkedList<ProductDTO> getFormattedDataProducts() {
		
		LinkedList<ProductDTO> productList = new LinkedList<>();
		
		for (int i = 0; i < productNameElements.size(); i++) {
			this.formatDataStructure(i);

			String productName = productNameElements.get(productIndex).text().replace("'", "");
			
			productList.add(ProductDTO.builder()
					   .productName(productName)
					   .shopName(shopName)
					   .category(category)
					   .price(price)
					   .url(productURLComplete)
					   .imageUrl(imageUrlElements.get(productIndex).attr(PropertyManager.getInstance().IMAGE_ATTRIBUTE))
					   .build());
		}
		
		return productList;
	}

	public void searchNewProducts(String shopName, String category, String url) {

		ArrayList<String> pagesArray = (ArrayList<String>) this.findPagesInSpecificCategory(url);

		pagesArray.stream().forEach(page -> {
			this.setConnection(page);
			this.getProductsFromPage();
			this.getFormattedDataProducts();
			// --- > this.formatDataStructure();

		});

		indexSearchPage = 0;
		if (initialized) {

			// this.findPageUrl(this.getDocument().select(PropertyReader.getInstance().getProperty("numberOfPages")),
			// initialized).stream().forEach(null);
			// ;

			for (int searchCounter = 0; searchCounter < numberOfPages; searchCounter++) {
				this.searchNextPage();

				this.getProductsFromPage();

				for (productIndex = 0; productIndex < productNameElements.size(); productIndex++) {



					ProductDTO productDTO = ProductDTO.builder().productName(productName).shopName(shopName)
							.category(category).price(price).url(productURLComplete).imageUrl(imageUrlElements
									.get(productIndex).attr(PropertyReader.getInstance().getProperty("imageAttribute")))
							.build();

					productRepository.save(ProductMapper.mapToProduct(productDTO));
					/*
					 * products.add(new Product(productName.get(productIndex).text().replace("'",
					 * ""), this.getShopName(), this.getCategory(), this.getCategoryEnum(),
					 * productURLComplete,
					 * imageUrlElements.get(productIndex).attr(PropertyReader.getInstance().
					 * getProperty("imageAttribute")), price));
					 */
				}
				// productDatabaseHandler.saveAll(products);
			}
		}
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

	public String searchProduct(String shopName, String html, String category) {
		this.startSearching(html);
		this.setCategory(category);
		// this.setConnection();
		initialized = true;
		// browserActivated = true;
		this.searchNewProducts(shopName, category);
		return "";
	}

	public ArrayList<Product> getProductsArray() {
		return this.products;
	}

	public void setSpecificInformations(String category) {
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getCategory().equals(category))
				specificProducts.add(products.get(i));
		}
	}

	public void clearProductsArray() {
		this.specificProducts.clear();
	}
}