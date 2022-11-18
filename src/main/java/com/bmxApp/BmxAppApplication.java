package com.bmxApp;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringBootApplication
public class BmxAppApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(BmxAppApplication.class, args);

		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("lang/res");
		//System.out.println(messageSource.getMessage("hello", null, Locale.getDefault()));
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setDefaultLocale(new Locale.Builder().setLanguage("pl").setScript("Latn").setRegion("PL").build());
		return localeResolver;
	}

	// Register the locale it found as the current user’s locale
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		// Defaults to "locale" if not set
		localeChangeInterceptor.setParamName("localeData");
		return localeChangeInterceptor;
	}

	// Override the addInterceptors method and add our locale change interceptor to
	// the registry
	@Override
	public void addInterceptors(InterceptorRegistry interceptorRegistry) {
		interceptorRegistry.addInterceptor(localeChangeInterceptor());
	}
}
