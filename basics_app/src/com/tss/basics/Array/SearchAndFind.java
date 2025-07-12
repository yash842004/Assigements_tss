package com.tss.basics.Array;

import java.util.Scanner;

public class SearchAndFind {

	public static void main(String[] args) {

//1601		
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the value");
		int input_user = scanner.nextInt();
		int[] number = new int[4];
		number[0] = 23;
		number[1] = 24;
		number[2] = 25;
		number[3] = 26;

		for (int i = 0; i < 5; i++){
			if(number[i] == input_user) {
				System.out.println("It find the number: "+input_user);
				break;
			}else {
				System.out.println("The provided number is not in the list");
				break;
			}
				
			
		}
		
	}

}
