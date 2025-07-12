package com.tss.basics.while_dowhile;

import java.util.Random;

public class SwitchCases {

	public static void main(String[] args) {
		Random random = new Random();
		int number = random.nextInt(6);
		System.out.println("Generated the number "+number);

		switch (number) {
		case 1:
			System.out.println("ONE");
			break;
		case 2:
			System.out.println("TWO");
			break;
		case 3:
			System.out.println("THREE");
			break;
		case 4:
			System.out.println("FOUR");
			break;
		case 5:
			System.out.println("FIVE");
			break;
			
		default:
			System.out.println("Out of 5");

		}

	}

}
