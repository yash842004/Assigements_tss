package com.tss.basics.Array;

import java.util.Scanner;

public class FindSubString {

	public static void main(String[] args) {

		boolean result = subStringMatcher();
		System.out.println(result);

	}

	static boolean subStringMatcher() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter the String: ");
		String name = scanner.next();
		System.out.println("Enter the SubString: ");
		String subName = scanner.next();

		char[] arrayName = new char[name.length()];
		char[] arraySubName = new char[name.length()];
		char[] result = new char[name.length()];

		int pointer1 = 0;
		int pointer2 = 0;

		for (int i = 0; i < name.length(); i++) {
			arrayName[i] = name.charAt(i);

		}
		System.out.println();
		for (int i = 0; i < subName.length(); i++) {
			arraySubName[i] = subName.charAt(i);

		}
		int match = 0;
		for (int i = 0; i < name.length(); i++) {
			if (arrayName[pointer1] == arraySubName[pointer2]) {
				result[match] = arraySubName[pointer2];
				pointer1++;
				pointer2++;
				match++;
			}

			else if (arrayName[pointer1] != arraySubName[pointer2]) {
				pointer1++;

			}

		}
		System.out.println();

		if (match == subName.length()) {
			return true;
		} else {
			return false;
		}

	}

}
