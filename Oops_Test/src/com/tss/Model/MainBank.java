package com.tss.Model;

import java.util.Scanner;

public class MainBank {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		Bank account1 = new Bank();
		boolean condition = true;
		while (condition) {

			Gathering();
			int number = scanner.nextInt();
			switch (number) {
			case 1:

				System.out.println("Enter the Account Id");
				account1.setAccountId(scanner.nextInt());
				scanner.nextLine();
				System.out.println("Enter the Account number");
				account1.setAccountId(scanner.nextInt());
				scanner.nextLine();
				System.out.println("Enter the Account name");
				account1.setName(scanner.next());
				scanner.nextLine();
				System.out.println("Enter the Account balance");
				account1.setBalance(scanner.nextInt());
				scanner.nextLine();
				System.out.println("Enter the Account Type");
				System.out.println("Enter 1. for Saving 2. for Current 3. for FD");
				int result = scanner.nextInt();
				if (result == 1) {
					System.out.println("It is Saving number " + account1.getAccountType().SAVING);
				}
				if (result == 2) {
					System.out.println("It is current number " + account1.getAccountType().CURRENT);
				}

				scanner.nextLine();
				break;

			case 2:
				System.out.println("balance: " + account1.getBalance());
				break;
			case 3:
				System.out.println("deposit ");
				account1.deposit();

				break;
			case 4:
				System.out.println("withdraw ");
				account1.withdraw();
				break;
			case 5:
				condition = false;
				System.out.println("Exit");

				break;
			}
		}

	}

	public static void Gathering() {
		System.out.println("Enter 1 for Creating account");
		System.out.println("Enter 2 for Display balance");
		System.out.println("Enter 3 for deposit");
		System.out.println("Enter 4 for withdraw");
		System.out.println("Enter 5 for Exit");
	}

}
