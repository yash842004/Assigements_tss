package com.tss.basics.while_dowhile;

import java.util.Random;
import java.util.Scanner;

public class PigGame {

	static void playAgain() {
		Scanner sc = new Scanner(System.in);
		System.out.println("DO you want to play again yes or no");
		String play_agian = sc.next();
		while (true)
			if (play_agian.equals("yes")) {
				RandomGusses();
			} else if (play_agian.equalsIgnoreCase("no")) {
				System.out.println("Thank you");
				break;
			}
	}

	static void RandomGusses() {
		Random rand = new Random();
		Scanner sc = new Scanner(System.in);
		int To_win = 20;

		int Total_points = 0;
		int total_attempts = 0;

		for (int i = 0; i < 5; i++) {
			System.out.println("Roll(r) or hold(h)");
			String input_user = sc.next();
			total_attempts = total_attempts + 1;

			int dice = rand.nextInt(1, 7);
			Total_points = Total_points + dice;
			System.out.println("dice " + dice);
			System.out.println("Total_points " + Total_points);

			if (dice == 1) {
				Total_points = 0;
				System.out.println("you burn all your points " + Total_points);
				playAgain();
				return;

			}

			else if (Total_points >= 20) {
				System.out.println("Your win the game");
				System.out.println("Total attempts are " + total_attempts);
				playAgain();
				return;

			} else if (input_user.equalsIgnoreCase("h")) {
				System.out.println("Total points are " + Total_points);
				playAgain();
				break;

			}
		}

		System.out.println("Your attenpts are over ");

	}

	public static void main(String[] args) {
		RandomGusses();

	}

}
