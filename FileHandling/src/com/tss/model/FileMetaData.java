package com.tss.model;

import java.io.File;

public class FileMetaData {

	public static void listFilesInDirectory(File directory) {

		if (directory.isDirectory()) {

			File[] files = directory.listFiles();

			if (files != null) {
				for (File file : files) {
					if (file.isFile()) {
						System.out.println(file.getAbsolutePath());
					} else if (file.isDirectory()) {
						listFilesInDirectory(file);
					}
				}
			}
		} else {
			System.out.println("Error: " + directory.getAbsolutePath() + " is not a directory.");
		}
	}

	public static void main(String[] args) {

		boolean whatItis = true;
		File file = new File("C:\\Users\\yash.bhimani\\Eclipes\\Ass\\FileHandling\\direc1");

		if (file.isFile()) {

			System.out.println(file.length());

		}

		if (file.isDirectory()) {
			listFilesInDirectory(file); // Call the method to list files
		}
	}

}
