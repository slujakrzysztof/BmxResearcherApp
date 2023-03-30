package com.bmxApp.creator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class PathCreator {

	private final static String DISCOUNT = "discountValue";
	private final static String SORT = "sortedBy";

	private final static String SCHEME = "http";
	private final static String HOST = "localhost";
	private final static int PORT = 8080;

	public static Map<String, String> getParams(String queryParams) {

		Map<String, String> map = new HashMap<>();

		List<String> queries = Arrays.asList(queryParams.split("&"));

		queries.forEach(query -> map.put(query.split("=")[0], query.split("=")[1]));

		return map;
	}

	public static List<NameValuePair> removeParam(List<NameValuePair> queryParams, String param) {

		List<NameValuePair> list = queryParams.stream().filter(parameter -> !parameter.getName().equals(param))
				.collect(Collectors.toList());

		return list;
	}

	public static String createSearchUri(HttpServletRequest request) {

		Map<String, String> map = getParams(request.getQueryString());

		List<NameValuePair> paramsList = map.entrySet().stream()
				.map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList());

		URIBuilder builder = new URIBuilder().setScheme(SCHEME).setHost(HOST).setPort(PORT)
				.setPath(request.getRequestURI()).addParameters(paramsList);
		URI uri;

		try {
			uri = builder.build();
		} catch (URISyntaxException e) {
			uri = null;
			e.printStackTrace();
		}

		return uri.toString();

	}

	public static String getCurrentUrl(HttpServletRequest request) {

		if (request.getQueryString() == null)
			return request.getRequestURL().toString();
		return request.getRequestURL() + "?" + request.getQueryString();
	}

	public static String createDiscountUrl(String currentUrl, String discountValue) {

		String url = currentUrl;

		try {
			URIBuilder builder = new URIBuilder(currentUrl);

			builder.setParameter(DISCOUNT, discountValue);

			if (discountValue.equals("0"))
				builder.setParameters(removeParam(builder.getQueryParams(), DISCOUNT));

			// builder.setParameters(removeParam(builder.getQueryParams(), DISCOUNT));
			// builder.addParameter(DISCOUNT, discountValue);

			url = builder.build().toString();

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return url;
	}

	public static String createSortUrl(String currentUrl, String sortValue) {

		String url = currentUrl;

		try {
			URIBuilder builder = new URIBuilder(currentUrl);

			builder.setParameters(removeParam(builder.getQueryParams(), SORT));
			builder.addParameter(SORT, sortValue);

			url = builder.build().toString();

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return url;
	}

}
