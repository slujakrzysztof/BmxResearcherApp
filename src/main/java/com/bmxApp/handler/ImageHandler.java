package com.bmxApp.handler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

@Component
public class ImageHandler {

	private URL imageUrl;
	@Value("")
	private String imageName;
	private String fileName = "image.jpg";
	@Value("images/")
	private String imageFolder;
	private int imageWidth;
	private int imageHeigth;

	private BufferedImage image;
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	MultipartFile multipartFile;
	Path path;

	public void setImageUrl(String imageUrl) {
		try {
			this.imageUrl = new URL(imageUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public URL getImageURL() {
		return this.imageUrl;
	}

	private void saveImage() {
		try {
			System.out.println("FOLDER: " + imageFolder);
			image = ImageIO.read(getImageURL());
			ImageIO.write(image, "jpg", byteArrayOutputStream);
			byteArrayOutputStream.flush();
			multipartFile = new MockMultipartFile(fileName, byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
			path = Paths.get(imageFolder);
			Files.copy(multipartFile.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getImage() {
		saveImage();
	}

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
		return new int[] { imageWidth, imageHeigth };
	}
}
