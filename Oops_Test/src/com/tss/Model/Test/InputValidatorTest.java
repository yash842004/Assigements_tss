package com.tss.Model.Test;

import java.util.Scanner;

import com.tss.Model.InputValidator;

public class InputValidatorTest {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String username, password, email;

		while (true) {
			System.out.print("Enter username: ");
			username = sc.nextLine();
			if (InputValidator.validateUsername(username)) {
				System.out.println("Username is valid.");
				break;
			}
			System.out.println("Invalid username. It must be 3-20 characters long and not empty.");

		}

		while (true) {
			System.out.print("Enter password: ");
			password = sc.nextLine();
			if (InputValidator.validatePassword(password)) {
				System.out.println("Password is valid.");
				break;
			}
			System.out.println("Invalid password. It must be 8-30 characters long and not empty.");

		}

		while (true) {
			System.out.print("Enter email: ");
			email = sc.nextLine();
			if (InputValidator.validateEmail(email)) {
				System.out.println("Email is valid.");
				break;
			}
			System.out.println("Invalid email. It must be 5-50 characters long and contain '@' and '.'");

		}

		sc.close();
	}
}
