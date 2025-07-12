package com.tss.basics.Array;

import java.util.Scanner;

public class ArrayNumbers {

	public static void main(String[] args) {

		int[] number = new int[5];

		Scanner scanner = new Scanner(System.in);

		for (int i = 0; i < 5; i++) {
			System.out.println("Enter the Elements: ");
			number[i] = scanner.nextInt();
		}
		for (int i = 0; i < 5; i++) {

			System.out.printf("\t " + number[i]);
		}
	}

}
