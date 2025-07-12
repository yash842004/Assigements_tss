package com.tss.basics;
import java.util.Scanner;

public class DistanceBetweenTwoPoints {
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter x1 value ");
		Double x1 = sc.nextDouble();
		System.out.println("Enter x2 value ");
		Double x2 = sc.nextDouble();
		System.out.println("Enter y1 value ");
		Double y1 = sc.nextDouble();
		System.out.println("Enter y2 value ");
		Double y2 = sc.nextDouble();
		
		
		double distance = Math.sqrt(Math.pow(x2 - x1, 2)+Math.pow(y2 - y1,2));
		System.out.println("Distance is "+distance);
				 

	}
	

}
