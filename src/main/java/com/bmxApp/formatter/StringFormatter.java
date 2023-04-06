package com.bmxApp.formatter;

import java.util.Arrays;
import java.util.List;

public class StringFormatter {

	private static final String SPLITTER = "<br>";
	private static final int LINE_LENGTH = 50;
	private static final int WORDS = 7;

	public static String formatCompareDescription(String description) {

		List<String> lines = Arrays.asList(description.split(SPLITTER));

		for (String line : lines) {

			if (line.length() >= LINE_LENGTH) {
				int index = lines.indexOf(line);
				lines.set(index, addLineSeparator(line));
			}

		}

		String desc = String.join(System.lineSeparator(), lines);

		return desc;
	}

	public static String addLineSeparator(String line) {

		int counter = 0;
		int amount = WORDS;
		List<String> words = Arrays.asList(line.split("\\s+"));
		StringBuilder changingLine = new StringBuilder();

		while (true) {
			
			while (counter < amount) {

				changingLine.append(words.get(counter)).append(" ");
				counter += 1;
			}
			changingLine.append(System.lineSeparator());

			if ((words.size() - counter) > 0 && (words.size() - counter) < WORDS)
				amount += (words.size() - amount);
			else
				amount += WORDS;

			if ((words.size() - counter) <= 0)
				break;
		}

		System.out.println("CHANGED: " + changingLine.toString());

		return changingLine.toString();

	}

}
