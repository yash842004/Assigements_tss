package com.tss.basics.Array;

import java.util.Scanner;

public class AverageIsPresent {

	static boolean isAverage() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Enter the size of array: ");
		int size = scanner.nextInt();
		int[] numbers = new int[size];

		int sum = 0;

		for (int i = 0; i < size; i++) {
			System.out.println("Enter element " + (i + 1) + ": ");
			numbers[i] = scanner.nextInt();
			sum += numbers[i];
		}

		int average = sum / size;

		for (int i = 0; i < size; i++) {
			if (numbers[i] == average) {
				System.out.println("Average found: " + average);
				return true;
			}
		}

		System.out.println("Average not found in array.");
		return false;
	}

	public static void main(String[] args) {

		boolean result = isAverage();
		System.out.println(result);
		

	}
}
