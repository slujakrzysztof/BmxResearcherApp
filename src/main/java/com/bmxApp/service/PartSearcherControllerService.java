package com.bmxApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmxApp.handler.ImageHandler;
import com.bmxApp.model.Product;
import com.bmxApp.model.ShopProduct;
import com.bmxApp.researcher.ShopResearcher;

@Service
public class PartSearcherControllerService {

	private ArrayList<Product> productArray = new ArrayList<Product>();

	private String number;
	private String productName;
	private double price;
	private String imageUrl;

	@Autowired
	DatabaseService databaseService;

	@Autowired(required = false)
	ShopResearcher shopResearcher;

	@Autowired
	ImageHandler imageHandler;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	/*
	 * public String getProductName() { return part; }
	 * 
	 * public void setPart() { this.part = part; }
	 */

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImageUrl(String productName) {
		imageUrl = ((ShopProduct) databaseService.getProductByName(productName, shopResearcher.getShopName()))
				.getImageUrl();
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImage(String productName) {
		imageHandler.setImageSize(300, 300);
		imageHandler.setImageName(
				((ShopProduct) databaseService.getProductByName(productName, shopResearcher.getShopName()))
						.getImageUrl());
		return imageHandler.getImageName();
	}

	public List<Product> getProductsByCategoryAndShopName(String category, String shopName) {
		return databaseService.getProductsByCategoryAndShopName(category, shopName);
	}

	/*
	 * class ShopPanel{
	 * 
	 * JLabel numberLabel = new JLabel(); JLabel partLabel = new JLabel(); JLabel
	 * priceLabel = new JLabel();
	 * 
	 * private final int NUMBER_COLUMN = 0, PART_COLUMN = 1, PRICE_COLUMN = 2,
	 * IMAGE_COLUMN = 3;
	 * 
	 * final String[] TABLE_COLUMNS = {"Nr","Part","Price","Image"}; private static
	 * final int ROW_HEIGHT = 100;
	 * 
	 * private JButton productButton;
	 * 
	 * final DefaultTableModel tableModel = new DefaultTableModel(TABLE_COLUMNS, 4){
	 * 
	 * @Override public Class<?> getColumnClass(int column) { if (column==3) return
	 * ImageIcon.class; return Object.class; } }; JTable table;
	 * 
	 * 
	 * public int getPriceColumn(){ return this.PRICE_COLUMN; }
	 * 
	 * public void setLabel(ArrayList<ShopProduct> arrayL) {
	 * 
	 * for(int i = 0; i < arrayL.size() - 1; i++) {
	 * imageHandler.setImage(arrayL.get(i).getProductDetails()[3]);
	 * imageHandler.resizeImage();
	 * 
	 * Object[] data = {"" + (i+1), arrayL.get(i).getProductDetails()[0],
	 * arrayL.get(i).getProductDetails()[1], imageHandler.getImage()};
	 * tableModel.addRow(data); } this.add(table); }
	 * 
	 * public DefaultTableModel getJTableModel() { return this.tableModel; }
	 * 
	 * public JTable getTable() { return this.table; }
	 * 
	 * }
	 */
}
