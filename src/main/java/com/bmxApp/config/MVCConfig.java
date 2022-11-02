package com.bmxApp.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		showImageDirectory("images", registry);
	}

	private void showImageDirectory(String directory, ResourceHandlerRegistry registry) {
		String path = Paths.get(directory).toFile().getAbsolutePath();
        if (directory.startsWith("../")) directory = directory.replace("../", "");
        registry.addResourceHandler("/" + directory + "/**").addResourceLocations("file:/"+ path + "/");
	}
}
