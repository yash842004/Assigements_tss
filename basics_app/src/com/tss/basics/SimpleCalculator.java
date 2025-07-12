package com.tss.basics;
import java.util.Scanner;

public class SimpleCalculator {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter first number");
		int number1 = sc.nextInt();
		System.out.println("Enter second number");
		int number2 = sc.nextInt();

	

		
		addition(number1,number2);
		subtration(number1,number2);
		multiplication(number1,number2);
		division(number1,number2);
		
		
	}
	
	private static void addition(int number1, int number2) {
		int sum = number1 + number2;	
		System.out.println("sum = " +sum); 
	}
	
	

	private static void subtration(int number1,int  number2) {

		int difference = number1 - number2;
			System.out.println("difference = " +difference);
	}

	private static void multiplication(int number1,int number2) {
		int multiplication = number1 * number2;
		System.out.println("multiplication = " +multiplication);
	}

	private static void division(int number1,int number2) {
		int division = number1 / number2;
		System.out.println("division = " +division);
	}

}
