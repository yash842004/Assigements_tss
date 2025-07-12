package com.tss.Model.ExceptionHandling;

import java.util.InputMismatchException; // Import for specific exception
import java.util.Scanner;

public class Exception {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		try {
			System.out.println("Enter the number 1:");
			int number1 = scanner.nextInt();

			System.out.println("Enter the number 2:");
			int number2 = scanner.nextInt();

			int result = number1 / number2;
			System.out.println("The result is " + result);

		} catch (InputMismatchException e) {
			System.out.println("Invalid input. Please enter an integer.");
		} catch (ArithmeticException e) {
			System.out.println("Error: Cannot divide by zero.");
		} catch (java.lang.Exception e) {
			System.out.println("An unexpected error occurred: " + e.getMessage());
		}
	}
}