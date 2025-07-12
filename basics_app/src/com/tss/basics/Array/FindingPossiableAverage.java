package com.tss.basics.Array;

import java.util.Scanner;

public class FindingPossiableAverage {

	static boolean hasAverageInArray(int[] numbers) {
		int sum = 0;
		for (int i = 0; i < numbers.length; i++) {
			sum += numbers[i];
		}

		int average = sum / numbers.length;

		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] == average) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter the size of array");
		int sizeOfArray = scanner.nextInt();
		int[] number = new int[sizeOfArray];

		System.out.println("Enter the elements:");
		for (int i = 0; i < sizeOfArray; i++) {
			number[i] = scanner.nextInt();
		}
		System.out.println(hasAverageInArray(number));
		
		
	}
}
