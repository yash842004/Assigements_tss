package com.tss.basics;

import java.util.Scanner;

public class AreaOfCircle {
	

	public static void main(String[] args) {

	 double pi =  3.14;
	  
	 Scanner sc = new Scanner(System.in);
	 System.out.println("Enter the value of radious ");
	 int Radius = sc.nextInt();
	 double AreaOfCircle = pi*Radius*Radius;
	 System.out.println("Area of Circle = "+AreaOfCircle);
	 
	 
		

	}

}
