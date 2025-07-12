package com.tss.basics.while_dowhile;

import java.util.Random;
import java.util.Scanner;

public class NumberGusser {
	
	
	static void numberGusser() {
		Random rand =  new Random();
		Scanner sc = new Scanner(System.in);
		
		int result = rand.nextInt(1, 101);
		System.out.println(result);
		int scores = 0;
		for(int i = 0; i < 5; i++) {
			System.out.println("Guess the number between 1-100");
			int input_user = sc.nextInt();
			

			if(input_user < result) {
				System.out.println("Your Guess is too low");
				
			}
			else if(input_user == result) {
				System.out.println("You win the");
				System.out.println("Generated number is  "+result);
				System.out.println("Gussed number is  "+result);
				scores = i+1;
				System.out.println("The number of attempts "+scores);
				
				break;
			}else {
				System.out.println("Your Guess is too high");

			}
			
			
		}
		System.out.println("Your attempts are over");
		
	}

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		numberGusser();
		System.out.println("want to play again yes or no");
		String playAgain = sc.next();
		
		while(true) {
		if(playAgain.equalsIgnoreCase("yes")) {
			numberGusser();
		}else if(playAgain.equalsIgnoreCase("no")) {
			System.out.println("Thank you");
			break;
		}
		
		}
	}

}
