package com.bmxApp.properties;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class PropertyReader {
	
	private final String LOCATION_PREFIX = "com/bmxApp/properties/";
	private final String FILE_EXTENSION = ".properties";
	
	private String propertyFilename;
	private static final PropertyReader propertyReader = new PropertyReader();
	private Properties property = new Properties();
	ClassLoader loader = Thread.currentThread().getContextClassLoader();  

	// 
	public void saveProperty(String key, String newValue){
	    FileOutputStream output;
	    try {
	        output = new FileOutputStream("properties/config.properties");
	        property.setProperty(key, newValue);
	        property.store(output, null);
	        output.close();
	    } catch(FileNotFoundException ex1) {
	        ex1.printStackTrace();
	    } catch(IOException ex2) {
	        ex2.printStackTrace();
	    }
	}
	
	public void connectPropertyReader(String shopName) {
		
		String filename = LOCATION_PREFIX + shopName + FILE_EXTENSION;
		this.setPropertyFilename(filename);
		this.setConnection();
	}

	// Connecting with given file with data
	public void setConnection(){
	    
	    InputStream input = null;
	    try {
	        input = loader.getResourceAsStream(this.getPropertyFilename());//getClass().getClassLoader().getResourceAsStream(getPropertyFilename());//
	        property.load(input); 
	    } catch(IOException ex) {
	        ex.printStackTrace();
	    } catch(NullPointerException ex2){

	    }finally {
	        if(input != null) {
	            try {
	                input.close();
	            } catch(IOException ex1) {
	                ex1.printStackTrace();
	            }
	        }
	    }
	}

	//Getting property by specific key
	public String getProperty(String key){
	    return property.getProperty(key);
	}

	//Thanks to this method there is only one instance of class and user can get it
	public static PropertyReader getInstance(){
	    return propertyReader;
	}
}
