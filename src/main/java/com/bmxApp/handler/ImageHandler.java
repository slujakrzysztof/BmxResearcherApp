package com.bmxApp.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ImageHandler {

	@Value("${image.pathUrl}")
	private String imageUrl;
	@Value("")
	private String imageName;
	private int imageWidth;
	private int imageHeigth;

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setImageSize(int imageWidth, int imageHeight) {
		this.imageWidth = imageWidth;
		this.imageHeigth = imageHeight;
	}

	public String getImageName() {
		return this.imageName;
	}
	
	public int[] getImageSize() {
		return new int[] {imageWidth, imageHeigth};
	}
}
