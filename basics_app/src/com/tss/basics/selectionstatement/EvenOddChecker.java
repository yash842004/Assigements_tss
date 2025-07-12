package com.tss.basics.selectionstatement;

import java.util.Scanner;

public class EvenOddChecker {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		int number1 = sc.nextInt();
		
		if(number1 % 2 ==0) {
			System.out.println("It is a even number");
		}else {
		System.out.println("It is a odd number");
		}
	}

}
