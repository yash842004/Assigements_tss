package com.tss.basics.Array;

import java.util.Scanner;

public class characterReplaceSolution {

	 public static void main(String[] args) {

	        Scanner scanner = new Scanner(System.in);

	        System.out.println("Enter the number to skip ");
	        int number = scanner.nextInt();
	        System.out.println("Enter the String ");
	        String inputString = scanner.next();
	        char[] storeStringValue = new char[inputString.length()];
	        int[] charArrayToAscii = new int[storeStringValue.length];
	        char[] result = new char[charArrayToAscii.length];

	        for (int i = 0; i < inputString.length(); i++) {
	            storeStringValue[i] = inputString.charAt(i);
	            System.out.print(storeStringValue[i]);
	        }
	        System.out.println();

	        for (int i = 0; i < storeStringValue.length; i++) {
	            charArrayToAscii[i] = storeStringValue[i];

	        }

	        if (number < 0 || number >= 0) {
	            for (int i = 0; i < charArrayToAscii.length; i++) {
	                charArrayToAscii[i] = charArrayToAscii[i] + number;
	                result[i] = (char) charArrayToAscii[i];
	            }

	        } else {
	            for (int i = 0; i < inputString.length(); i++) {
	                storeStringValue[i] = inputString.charAt(i);
	                System.out.print(storeStringValue[i]);
	            }

	        }

	        String finalResult = new String(result);
	        System.out.println(finalResult);

	    }

}
