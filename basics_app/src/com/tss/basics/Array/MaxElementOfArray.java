package com.tss.basics.Array;

import java.util.Scanner;

public class MaxElementOfArray {

	public static void main(String[] args) {
		int max = 0;

		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the size of array ");
		int user_input = scanner.nextInt();
		int[] number = new int[user_input];

		for (int i = 0; i < (user_input - 1); i++) {
			System.out.println("Enter the Elements: ");
			number[i] = scanner.nextInt();
		}
		for (int i = 0; i < user_input - 1; i++) {
			if (number[i] > number[i + 1]) {

			}

		}
//		System.out.printf("result " + result);

	}

}
