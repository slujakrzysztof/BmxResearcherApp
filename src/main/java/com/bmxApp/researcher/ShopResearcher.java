package com.bmxApp.researcher;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.bmxApp.model.Product;
import com.bmxApp.model.ShopProduct;
import com.bmxApp.properties.PropertyReader;
import com.bmxApp.service.DatabaseService;


@Component
@Scope("prototype")
public class ShopResearcher {

	final int MAX_TRIES = 20;
	private int numberOfPages;
	private String html;
	protected Document doc;
	private int tryCounter = 0;
	private boolean initialized, browserActivated;
	private Elements div, productName, productPrice, productURL, imageURL, pages;
	private int productIndex = 0, productIndexURL = 0, productImageIndex = 0, indexSearchPage = 0;
	private String category;
	private String shopName;

	Document.OutputSettings outputSettings = new Document.OutputSettings();
	String[] shopArray = { "bmxlife", "avebmx", "manyfestbmx", "allday" };

	ArrayList<com.bmxApp.model.Product> products = new ArrayList<>();
	ArrayList<Product> specificProducts = new ArrayList<>();
	ArrayList<String> pagesArray = new ArrayList<>();
	ArrayList<String> pagesArrayAve = new ArrayList<>();

	@Autowired
	DatabaseService databaseService;

	/*public ShopResearcher(String html, String shopName) {
		this.html = html;
		this.shopName = shopName;
	}*/

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

	public void searchPage(String partName) {
		Elements partPage = doc.select(PropertyReader.getInstance().getProperty("urlSearch"));// doc.select("div.category-miniature.no-image
																								// > p > a[href]");
		System.out.println("JESTEM TU KURWWY: " + partPage);

		for (Element e : partPage) {
			if (e.absUrl("href").contains(partName)) {
				setHTML(e.absUrl("href"));
				System.out.println("ZNALEZIONO " + e.absUrl("href"));
				return;
			}
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
		pages = doc.select(PropertyReader.getInstance().getProperty("pageSearchElementMain"));
		// System.out.println("SIEEEEEEEEEEMA CO TAM KUREWKI: " +
		// pages.select(this.frame.getPropertyReader().getProperty("pageSearchElementSub")));
		numberOfPages = 0;
		for (int i = 0; i < pages.first().select(PropertyReader.getInstance().getProperty("pageSearchElementSub"))
				.text().replaceAll("\\s+", "").length(); i++) {
			if (Character.isDigit(pages.first().select(PropertyReader.getInstance().getProperty("pageSearchElementSub"))
					.text().replaceAll("\\s+", "").charAt(i)))
				numberOfPages += 1;
		}
		System.out.println("ILOSC STRON: " + numberOfPages);
		pagesArray.add(pages.first().select(PropertyReader.getInstance().getProperty("pageSearchElementSub")).get(1)
				.absUrl(PropertyReader.getInstance().getProperty("pageSearchAttribute")).substring(0,
						pages.first().select(PropertyReader.getInstance().getProperty("pageSearchElementSub")).get(1)
								.absUrl(PropertyReader.getInstance().getProperty("pageSearchAttribute")).length() - 1)
				+ "1");
		for (int index = 1; index < numberOfPages; index++) {
			pagesArray.add(pages.first().select(PropertyReader.getInstance().getProperty("pageSearchElementSub"))
					.get(index).absUrl(PropertyReader.getInstance().getProperty("pageSearchAttribute")));
			System.out.println("IND: " + index + " NR STRON ALLDAY 1: " + pagesArray.get(index));
		}
	}

	public void searchNewProducts() {

		numberOfPages = 1;

		productIndex = 0;
		productIndexURL = 0;
		productImageIndex = 0;

		pagesArray.clear();

		this.setPagesArray();

		if (initialized) {

			// System.out.println("ILOSC STRON PRZED WYSZ " + numberOfPages);

			for (int searchCounter = 0; searchCounter < numberOfPages; searchCounter++) {

				if (numberOfPages > 1) {
					setHTML(pagesArray.get(indexSearchPage));
					if (indexSearchPage == pagesArray.size() - 1)
						indexSearchPage = pagesArray.size() - 1;
					else
						indexSearchPage++;
					System.out.println("ZMIANA STRONY");
					setConnection();
				}

				div = doc.select(PropertyReader.getInstance().getProperty("div"));
				productName = div.select(PropertyReader.getInstance().getProperty("productNameElement"));
				productPrice = div.select(PropertyReader.getInstance().getProperty("productPriceElement"));
				productURL = div.select(PropertyReader.getInstance().getProperty("productURLElement"));
				imageURL = doc.select(PropertyReader.getInstance().getProperty("imageURLElement"));

				for (productIndex = 0; productIndex < productName.size(); productIndex++) {

					products.add(new ShopProduct(productName.get(productIndex).text(), this.getShopName(),
							this.getCategory(), productURL.get(productIndex).text(), imageURL.get(productIndex).text(),
							Double.parseDouble(productPrice.get(productIndex).text())));

					products.get(productIndex)
							.setProductName(products.get(productIndex).getProductName().replace("'", ""));
				}
				databaseService.insertAllProducts(products);
			}
		}
	}
	
	public void searchPreviousProducts(String shopName, String category) {
		
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