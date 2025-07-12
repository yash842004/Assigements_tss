package com.tss.basics.selectionstatement;

import java.util.Scanner;

public class MaximumOfThreeNumber {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter number one");
		int number1 = sc.nextInt();
		System.out.println("Enter number two");
		int number2 = sc.nextInt();
		System.out.println("Enter number three");
		int number3 = sc.nextInt();
		
		
		if( number1 > number2 && number1 > number3) {
			System.out.println("Number one is greater then other numbers "+number1);
		}else if(number2 > number1 && number2 > number3) {
			System.out.println("Number two is greater then other numbers "+number2);
		}else {
			System.out.println("Number three is greater then other numbers "+number3);

		}
		
	}

}
