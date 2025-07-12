package com.tss.basics.selectionstatement;

import java.util.Scanner;

public class MaximumOfTwoNumbers {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter number frist");
		int number1 = sc.nextInt();
		System.out.println("Enter number second");
		int number2 = sc.nextInt();
		
		if (number1 > number2) {
			System.out.println("Number one is greater then number two "+ number1);
		}else {
			System.out.println("Number two is greater then number one " + number2);
		}

	}

}
