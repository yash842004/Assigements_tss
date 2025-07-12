package com.tss.basics.while_dowhile;

import java.util.Scanner;

public class SwitchMonth {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the Month");
		String Month = scanner.next().toLowerCase();
		
		switch(Month) {
		
		case "jan": case "feb" : case "mar": case "apr": case "may":
			System.out.println("Winter season");
			break;
		
		case "jun": case "jul": case "aug": case "sep":
			System.out.println("Summer season");
			break;
			
		case "oct": case "nov": case "dec":
			System.out.println("Monsoon season");
			break;
		
		default:
			System.out.println("not valid month");

			
		}	

	}

}
