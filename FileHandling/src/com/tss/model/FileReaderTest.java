package com.tss.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderTest {

	public static void main(String[] args) {

		int charCount = 0;
		int wordCount = 0;
		int lineCount = 1;
		boolean inWord = false;

		try {
			FileReader reader = new FileReader("Input.txt");
			int ch;
			boolean fileIsEmpty = true;

			while ((ch = reader.read()) != -1) {
				fileIsEmpty = false;
				System.out.print((char) ch);

				if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
					inWord = false;
					if (ch == '\n') {
						lineCount++;
					}
				} else {
					charCount++;
					if (!inWord) {
						wordCount++;
						inWord = true;
					}
				}
			}

			if (fileIsEmpty) {
				//lineCount = 0;
			} else if (charCount > 0 && lineCount == 1 && !inWord) {

			} else if (charCount > 0 && lineCount == 0) {

			}

			if (fileIsEmpty) {
				lineCount = 0;
			} else if (lineCount == 1 && charCount > 0 && ch != '\n') {
			}

		} catch (FileNotFoundException e) {
			System.err.println("Error: Input.txt not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading the file.");
			e.printStackTrace();
		}

		System.out.println("\n File Detail ");
		System.out.println("Number of characters: " + charCount);
		System.out.println("Number of words: " + wordCount);
		System.out.println("Number of lines: " + lineCount);
	}

}