package com.tss.Model;

import java.util.Scanner;

public class Account {

	static int accountCount = 0;
	static final int maximumNumber = 100;
	static Account[] accounts = new Account[maximumNumber];
	static int accountIdCounter = 1;

	public Account() {

	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	private int accountId;
	private String accountNumber;
	private String name;
	private AccountType accountType;
	private int balance;

	public Account(int accountId, String accountNumber, String name, AccountType accountType, int balance) {
		this.accountId = accountId;
		this.accountNumber = accountNumber;
		this.name = name;
		this.accountType = accountType;
		this.balance = balance;
	}
	
	public void deposit() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter amount to deposit: ");
		int amount = scanner.nextInt();
		balance += amount;
		System.out.println("Deposit successful. New balance: " + balance);
	}

	public void withdraw() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter amount to withdraw: ");
		int amount = scanner.nextInt();
		if (balance >= amount) {
			balance -= amount;
			System.out.println("Withdraw successful. New balance: " + balance);
		} else {
			System.out.println("Insufficient balance.");
		}
	}

	public void viewBalance() {
		System.out.println("Account: " + accountNumber + " | Balance: " + balance);
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", accountNumber=" + accountNumber + ", name=" + name
				+ ", accountType=" + accountType + ", balance=" + balance + "]";
	}

	public void transfer() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter FROM account number: ");
		String fromAccount = scanner.next();
		System.out.print("Enter TO account number: ");
		String toAccount = scanner.next();

		Account sender = findAccountByNumber(fromAccount);
		Account receiver = findAccountByNumber(toAccount);

		if (sender == null || receiver == null) {
			System.out.println("Invalid sender or receiver account.");
			return;
		}

		System.out.print("Enter amount to transfer: ");
		int amount = scanner.nextInt();
		if (sender.getBalance() > 500) {
			if (sender.getBalance() >= amount) {
				sender.setBalance(sender.getBalance() - amount);
				receiver.setBalance(receiver.getBalance() + amount);
				System.out.println("Transfer successful.");
			}
		} else {
			System.out.println("Insufficient balance.");
		}

	}

	static Account findAccountByNumber(String accountNumber) {

		for (int i = 0; i < accountCount; i++) {
			if (accounts[i].getAccountNumber().equals(accountNumber)) {
				return accounts[i];
			}
		}
		return null;
	}

	public static void createAccounts() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("How many accounts to create? ");
		int number = scanner.nextInt();
		for (int i = 0; i < number; i++) {
			if (accountCount >= maximumNumber) {
				System.out.println("Account limit reached!");
				return;
			}

			scanner.nextLine();
			System.out.print("Enter name: ");
			String name = scanner.nextLine();

			System.out.print("Enter initial balance: ");
			int balance = scanner.nextInt();

			System.out.print("Enter 1 for SAVING, 2 for CURRENT: ");
			int typeChoice = scanner.nextInt();

			AccountType type = (typeChoice == 1) ? AccountType.SAVING : AccountType.CURRENT;
			System.out.println("Your account type is " + type);

			String accountNumber;
			do {
				accountNumber = generateAccountNumber();
			} while (findAccountByNumber(accountNumber) != null);

			Account account = new Account(accountIdCounter++, accountNumber, name, type, balance);
			accounts[accountCount++] = account;

			System.out.println("Account Created:");
			System.out.println(account);
		}
	}

	static String generateAccountNumber() {
		int random = 10000 + (int) (Math.random() * 90000);
		return "AXIS1000" + random;
	}

}
