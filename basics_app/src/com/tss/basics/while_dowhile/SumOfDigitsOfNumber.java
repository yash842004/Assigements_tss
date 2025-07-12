package com.tss.basics.while_dowhile;

import java.util.Scanner;

public class SumOfDigitsOfNumber {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the number");
		int number1 = sc.nextInt();
		int sum = 0;
		while ( number1 != 0) {
			int digit = number1 % 10;
			sum += digit;
			number1 /= 10;
		}
		System.out.println("Sum of digits: " + sum);
	}

}
