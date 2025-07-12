package com.tss.ecommers;

import java.util.Scanner;

public class Invoice01Test {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter Invoice ID: ");
		int id = scanner.nextInt();
		scanner.nextLine();

		System.out.print("Enter Description: ");
		String description = scanner.nextLine();

		System.out.print("Enter Cost: ");
		double cost = scanner.nextDouble();

		System.out.print("Enter Discount Percent: ");
		double discountPercent = scanner.nextDouble();

		Invoice01 invoice = new Invoice01(id, description, cost, discountPercent);

		invoice.print();

		scanner.close();
	}
}
