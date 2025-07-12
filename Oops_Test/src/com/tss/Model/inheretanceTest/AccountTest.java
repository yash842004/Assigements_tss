package com.tss.Model.inheretanceTest;

import java.util.Scanner;

import com.tss.Model.inheretance.model.Account;
import com.tss.Model.inheretance.model.CurrentAccount;
import com.tss.Model.inheretance.model.SavingAccount;

public class AccountTest {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		Account account = null;

		while (true) {

			System.out.println("1. Create Current Account");
			System.out.println("2. Create Savings Account");
			System.out.println("3. Credit Amount");
			System.out.println("4. Debit Amount");
			System.out.println("5. Display Account Details");
			System.out.println("6. Exit");
			System.out.print("Enter choice: ");
			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				System.out.print("Enter Acc No: ");
				int AccNo = sc.nextInt();
				sc.nextLine();
				System.out.print("Enter Name: ");
				String Name = sc.nextLine();
				System.out.print("Enter Balance: ");
				double Balance = sc.nextDouble();
				System.out.print("Enter Overdraft Limit: ");
				double limit = sc.nextDouble();
				account = new CurrentAccount(AccNo, Name, Balance, limit);
				System.out.println("Current account created.");
				break;

			case 2:
				System.out.print("Enter Acc No: ");
				int sAccNo = sc.nextInt();
				sc.nextLine();
				System.out.print("Enter Name: ");
				String sName = sc.nextLine();
				System.out.print("Enter Balance: ");
				double sBalance = sc.nextDouble();
				System.out.print("Enter Minimum Balance: ");
				double minBal = sc.nextDouble();
				account = new SavingAccount(sAccNo, sName, sBalance, minBal);
				System.out.println("Savings account created.");
				break;

			case 3:
				if (account != null) {
					System.out.print("Enter amount to credit: ");
					double amt = sc.nextDouble();
					account.credit(amt);
				} else {
					System.out.println("Create an account first.");
				}
				break;

			case 4:
				if (account != null) {
					System.out.print("Enter amount to debit: ");
					double amt = sc.nextDouble();
					if (account instanceof CurrentAccount) {
						((CurrentAccount) account).debit(amt);
					} else if (account instanceof SavingAccount) {
						((SavingAccount) account).debit(amt);
					}
				} else {
					System.out.println("Create an account first.");
				}
				break;

			case 5:
				if (account != null) {
					account.displayDetails();
				} else {
					System.out.println("No account to display.");
				}
				break;

			case 6:
				System.out.println("Thank you for using the bank app.");
				sc.close();
				System.exit(0);

			}
		}
	}

}
