package com.bmxApp.formatter;

public class StringFormatter {

	private static final String SPLITTER = "<br>"; 
	
	public static String formatCompareDescription(String description, int index) {
		
		String[] lines = description.split(SPLITTER);
		
		String desc = String.join(System.lineSeparator(), lines);
		
		
		return desc;
	}
	
}
