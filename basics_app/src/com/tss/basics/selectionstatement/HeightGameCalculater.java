package com.tss.basics.selectionstatement;

import java.util.Scanner;

public class HeightGameCalculater {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the Height");
		Double height = sc.nextDouble();
		
	
		int cost = 0;
		
		if(height > 120) {
			
			System.out.println("can ride");
			System.out.println("Enter the age");
			int age = sc.nextInt();
			if(age < 12 ) {
				cost = cost + 5;
				
			}
			else if(age >=12 && age < 18) {
				cost= cost + 7;
			}
			else if(age >= 18) {
				cost = cost + 12;
			}
			else if(age >= 45 && age < 55) {
				cost = 0;
			}
			else if (age > 55) {
				cost = cost + 12;
			}
			
			System.out.print("Want photo yes/no\n");
			String string1 = sc.next();
			if(string1.equalsIgnoreCase("yes")) {
				cost = cost + 3;
			} 
			
		}
		else {
			System.out.println("can't ride");
		}
		System.out.println("Total cost is "+cost);

	}

}
