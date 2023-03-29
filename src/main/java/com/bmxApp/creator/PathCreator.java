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

	private final static String SEARCH_PREFIX = "search";
	private final static String SCHEME = "http";
	private final static String HOST = "localhost";
	private final static int PORT = 8080;

	public static Map<String, String> getParams(String queryParams) {

		Map<String, String> map = new HashMap<>();

		List<String> queries = Arrays.asList(queryParams.split("&"));

		queries.forEach(query -> map.put(query.split("=")[0], query.split("=")[1]));

		return map;
	}

	public static String createSearchHtml(HttpServletRequest request) {

		Map<String, String> map = getParams(request.getQueryString());

		List<NameValuePair> paramsList = map.entrySet().stream()
				.map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList());

		URIBuilder builder = new URIBuilder().setScheme(SCHEME).setHost(HOST).setPort(PORT).setPath(request.getRequestURI())
				.addParameters(paramsList);
		URI uri;

		try {
			uri = builder.build();
		} catch (URISyntaxException e) {
			uri = null;
			e.printStackTrace();
		}

		return uri.toString();

	}

}
