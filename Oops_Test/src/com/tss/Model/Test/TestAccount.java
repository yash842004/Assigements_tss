package com.tss.Model.Test;

import java.util.Scanner;
import com.tss.Model.Account;

public class TestAccount {
	static Scanner scanner = new Scanner(System.in);
	

	public static void main(String[] args) {

		Account account1 = new Account();
		boolean condition = true;
		printGuide();

		while (condition) {
			System.out.print("Enter choice: ");
			int choice = scanner.nextInt();

			switch (choice) {
			case 1:
				account1.createAccounts();
				System.out.println();
				printGuide();
				break;
			case 2:
				account1.viewBalance();
				break;
			case 3:
				account1.deposit();
				break;
			case 4:
				account1.withdraw();
				break;
			case 5:
				account1.transfer();
				break;
			case 6:
				condition = false;
				break;
			default:
				System.out.println("Invalid choice.");
			}
		}
	}

	static void printGuide() {
		System.out.println("Enter 1: Create account");
		System.out.println("Enter 2: View balance");
		System.out.println("Enter 3: Deposit");
		System.out.println("Enter 4: Withdraw");
		System.out.println("Enter 5: Transfer");
		System.out.println("Enter 6: Exit");
	}



}
