package com.tss.basics;
import java.util.Scanner;
public class SwappingNumber {
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter the first value for two variable ");
		int number01 = sc.nextInt();
		System.out.println("Enter the second value for two variable ");
		int number02 = sc.nextInt();
		System.out.println("Enter the first value for three variable ");
		int number1 = sc.nextInt();
		System.out.println("Enter the second value for three variable ");
		int number2 = sc.nextInt();
		
		swappingWithThreeVariale(number1,number2); // for three variable
		
		swappingWithTwoVariable(number01,number02);	// for two variable
		
	}
	
	private static void swappingWithThreeVariale(int number01, int number02) {
		
		
		System.out.println("Value of number01 befor swapping for Three variable "+number01);
		System.out.println("Value of number02 befor swapping for Three variable "+number02);
		int temp = number01;
		number01 = number02;
		number02 = temp;
		System.out.println("Value of number01 after swapping "+number01);
		System.out.println("Value of number02 after swapping "+number02);
	}
	
	private static void swappingWithTwoVariable(int number1, int number2) {
		System.out.println("befor swapping the number1 for two variable " +number1);
		System.out.println("befor swapping the number2 for two variable " +number2);
		number1 = number1 + number2;
		number2 = number1 - number2;
		number1 = number1 - number2;
		System.out.println("after swapping the number1 " +number1);
		System.out.println("after swapping the number2 " +number2);	
	}
}