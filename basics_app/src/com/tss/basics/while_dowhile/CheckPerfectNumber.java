package com.tss.basics.while_dowhile;

import java.util.Scanner;

public class CheckPerfectNumber {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the number");

	     int number = sc.nextInt();
	     int sum = 0;
	     int i = 1;
	     while(i < number)
	       {
	     	if (number % i == 0)
	 	        sum = sum + i;
	       }i++;

	     if (sum == number)
	       System.out.println (number + " Is a perfect number");
	     else
	       System.out.println (number + " Is not a perfect number");

	}

}
