package com.tss.basics;

import java.util.Scanner;

public class CaluclateSimpleInterst {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter the principle Amount ");
		Double  principalamount = sc.nextDouble();
		System.out.println("Enter rate of interst ");
		Double rateOfIntrest = sc.nextDouble();
		System.out.println("Enter the Time period");
		int timePeriod = sc.nextInt();
		
		Double result = ((principalamount * rateOfIntrest * timePeriod)/100);
		System.out.println("Simple Intrest is "+result);
		
		
		
	}

}
