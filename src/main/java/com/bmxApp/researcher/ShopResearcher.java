package com.bmxApp.researcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

import com.bmxApp.handler.ProductDatabaseHandler;
import com.bmxApp.model.Product;
import com.bmxApp.properties.PropertyReader;

@Component
@Scope("prototype")
public class ShopResearcher {

	final int MAX_TRIES = 20;
	private int numberOfPages;
	private String html;
	protected Document doc;
	private int tryCounter = 0;
	private boolean initialized, browserActivated, productUpdated = false;
	private Elements div, productName, productPrice, productURL, imageURL, pages;
	private int productIndex = 0, productIndexURL = 0, productImageIndex = 0, indexSearchPage = 0;
	private String category;
	private String categoryEnum;
	private String shopName;
	private boolean partFound = false;

	Document.OutputSettings outputSettings = new Document.OutputSettings();
	String[] shopArray = { "bmxlife", "avebmx", "manyfestbmx", "allday" };

	ArrayList<com.bmxApp.model.Product> products = new ArrayList<>();
	ArrayList<Product> specificProducts = new ArrayList<>();
	List<String> pagesArray = new ArrayList<>();
	ArrayList<String> pagesArrayAve = new ArrayList<>();

	List<Product> existingProducts;

	@Autowired
	ProductDatabaseHandler productDatabaseHandler;

	double price = 0;
	String productURLComplete;

	/*
	 * public ShopResearcher(String html, String shopName) { this.html = html;
	 * this.shopName = shopName; }
	 */

	public void setProductUpdated(boolean value) {
		this.productUpdated = value;
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

	public void setHTML(String html) {
		this.html = html;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public int getShopNumber(String name) {
		for (int i = 0; i < shopArray.length; i++)
			if (name.equals(shopArray[i]))
				return i;
		return -1;
	}

	private boolean findProductPage(Elements partPage) {
		for (Element e : partPage) {
			if (e.absUrl("href").contains(this.getCategory())) {
				this.setHTML(e.absUrl("href"));
				this.setConnection();
				return true;
			}
		}
		return false;
	}

	public void searchPage() {
		Elements partPage = doc.select(PropertyReader.getInstance().getProperty("urlSearch"));
		while (!partFound) {
			if (findProductPage(partPage))
				return;
			partPage = doc.select(PropertyReader.getInstance().getProperty("urlSearchFrames"));
		}
	}

	public void setConnection() {
		while (tryCounter < MAX_TRIES) {
			try {
				doc = Jsoup.connect(this.html).timeout(6000).userAgent(
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36")
						.get();
				break;
			} catch (IOException ex) {
				if (++tryCounter == MAX_TRIES) {
					// JOptionPane.showMessageDialog(null,
					// this.frame.getPropertyReader().getProperty("warningTimeoutInformation"),
					// this.frame.getPropertyReader().getProperty("warning"),
					// JOptionPane.ERROR_MESSAGE);
					throw new NullPointerException();
				}
			}
		}
	}

	// --- Get url to pages with products ---
	public void setPagesArray() {
		System.out.println("SETPAGESARRAY");
		pagesArray.clear();
		try {
			pages = doc.select(PropertyReader.getInstance().getProperty("numberOfPages"));
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

	public String getHTML() {
		return this.html;
	}

	public String getCategoryEnum() {
		return categoryEnum;
	}

	public void setCategoryEnum(String categoryEnum) {
		this.categoryEnum = categoryEnum;
	}

	private void getProductsFromPage() {
		div = doc.select(PropertyReader.getInstance().getProperty("div"));
		productName = div.select(PropertyReader.getInstance().getProperty("productNameElement"));
		productPrice = div.select(PropertyReader.getInstance().getProperty("productPriceElement"));
		productURL = div.select(PropertyReader.getInstance().getProperty("productURLElement"));
		// DLA MANYFEST DOC DLA INNYCH DIV
		if (this.getShopName().equals(com.bmxApp.enums.Shop.MANYFESTBMX.name().toLowerCase()))
			imageURL = doc.select(PropertyReader.getInstance().getProperty("imageURLElement"));
		else
			imageURL = div.select(PropertyReader.getInstance().getProperty("imageURLElement"));
		System.out.println("imageURL: " + imageURL);
	}

	private void searchNextPage() {
		if (numberOfPages > 1) {
			setHTML(pagesArray.get(indexSearchPage));
			if (indexSearchPage == pagesArray.size() - 1)
				indexSearchPage = pagesArray.size() - 1;
			else
				indexSearchPage++;
			System.out.println("ZMIANA STRONY");
			setConnection();
		}
	}

	private void formatDataStructure() {

		if (this.getShopName().equals(com.bmxApp.enums.Shop.MANYFESTBMX.name().toLowerCase())) {
			price = Double.parseDouble(productPrice.get(productIndex).attr("content"));
		} else {
			try {
				price = Double.parseDouble(productPrice.get(productIndex).text().replaceAll("[^\\d.]", ""));
			} catch (NumberFormatException ex) {
				price = Double.parseDouble(productPrice.get(productIndex)
						.select(PropertyReader.getInstance().getProperty("productDiscountPriceElement")).text()
						.replaceAll("[^\\d.]", ""));
			}
		}
		if (this.getShopName().equals(com.bmxApp.enums.Shop.AVEBMX.name().toLowerCase())) {
			productURLComplete = "https://avebmx.pl"
					+ productURL.get(productIndex).attr(PropertyReader.getInstance().getProperty("urlAtrribute"));
		} else {
			productURLComplete = productURL.get(productIndex)
					.attr(PropertyReader.getInstance().getProperty("urlAtrribute"));
		}
		System.out.println("productURLComplete: " + productURLComplete);

	}

	public void searchNewProducts() {

		indexSearchPage = 0;
		if (initialized) {
			System.out.println("WCIĄŻ SZUKAM");
			System.out.println("AAA: " + this.getHTML());

			for (int searchCounter = 0; searchCounter < numberOfPages; searchCounter++) {
				System.out.println("IN FOR");
				this.searchNextPage();
				System.out.println("PROPERTY: " + doc.location());

				this.getProductsFromPage();

				System.out.println("ILOSC PRODUKTOW: " + productName.size());

				for (productIndex = 0; productIndex < productName.size(); productIndex++) {
					this.formatDataStructure();

					products.add(new Product(productName.get(productIndex).text().replace("'", ""), this.getShopName(),
							this.getCategory(), this.getCategoryEnum(), productURLComplete,
							imageURL.get(productIndex).attr(PropertyReader.getInstance().getProperty("imageAttribute")),
							price));
					System.out.println("PRODUKT: " + imageURL.get(productIndex)
							.attr(PropertyReader.getInstance().getProperty("imageAttribute")));
					System.out.println(products.get(productIndex).toString());
				}
				productDatabaseHandler.saveAll(products);
			}
		}
	}

	public void searchPreviousProducts(String shopName, String category) {
		existingProducts = productDatabaseHandler.findByCategoryAndShopName(category, shopName);
		for (int searchCounter = 0; searchCounter < numberOfPages; searchCounter++) {
			this.searchNextPage();
			this.getProductsFromPage();
			for (productIndex = 0; productIndex < productName.size(); productIndex++) {
				this.formatDataStructure();
				Product product = new Product(productName.get(productIndex).text().replace("'", ""), this.getShopName(),
						this.getCategory(), this.getCategoryEnum(), productURLComplete,
						imageURL.get(productIndex).attr(PropertyReader.getInstance().getProperty("imageAttribute")),
						price);
				if (existingProducts.contains(product))
					continue;
				else {

				}
				System.out.println(products.get(productIndex).toString());
			}
		}
	}

	public String getDescription(String className) throws NullPointerException {
		String[] separator = className.split(",");
		String finalS, str;
		Document jsoupDoc;
		for (int i = 0; i < separator.length; i++) {
			finalS = doc.select(separator[i]).html();
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

	public String searchProduct(String html, String partName) {
		this.setHTML(html);
		this.setCategory(partName);
		this.setConnection();
		initialized = true;
		browserActivated = true;
		this.searchNewProducts();
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