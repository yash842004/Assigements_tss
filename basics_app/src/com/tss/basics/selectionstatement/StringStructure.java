package com.tss.basics.selectionstatement;

import java.util.Scanner;

public class StringStructure {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.println("type left or right");
		String string1 = sc.nextLine();

		if (string1.equalsIgnoreCase("left")) {
			System.out.println("type wait/swim");
			String string2 = sc.nextLine();
			if (string2.equalsIgnoreCase("wait")) {
				System.out.println("Reb/blue/yellow");

				String string3 = sc.nextLine();
				if (string3.equalsIgnoreCase("yellow")) {
					System.out.println("WIN");
				} else {
					System.out.println("Lost GAME OVER");
				}
				System.out.println("Lost GAME OVER");
			}
		} else {
			System.out.println("Lose GAME OVER");
		}
	}
}
