package com.tss.basics.while_dowhile;

import java.util.Scanner;

public class CheckPrimeNumber {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the number");

		int number1 = sc.nextInt();
		boolean check = false;

		if (number1 == 0 || number1 == 1) {
			check = true;
		}
		int i = 2;

		while (i <= number1 / 2) {

			if (number1 % i == 0) {
				check = true;
				break;
			}
			++i;
		}

		if (!check)
			System.out.println(number1 + " is a prime number.");
		else
			System.out.println(number1 + " is not a prime number.");
	}
}
