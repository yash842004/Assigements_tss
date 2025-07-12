package com.tss.basics.while_dowhile;

import java.util.Scanner;

public class PattenPrimeNumber {

	public static boolean isPrime(int number) {

		if (number < 2)
			return false;
		int count = 0;
		for (int i = 1; i <= number; i++) {
			if (number % i == 0)
				count++;
		}

		return count==2;
	}

	public static void printPrimeTriangle(int start, int end) {
		System.out.println("Prime numbers in  pattern ");
		int row = 1;
		int count = 0;
		for (int i = start; i <= end; i++) {
			if (isPrime(i)) {
				System.out.print(i + " ");
				count++;
				if (count == row) {
					System.out.println();
					row++;
					count = 0;
				}
			}
		}
		if (count > 0)
			System.out.println();
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.print("Enter the start of the range: ");
		int start = sc.nextInt();
		System.out.print("Enter the end of the range: ");
		int end = sc.nextInt();
		if (start > end) {
			System.out.println("Start should be less than or equal to end.");
		} else {
			printPrimeTriangle(start, end);
		}

	}

}
