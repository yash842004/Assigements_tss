package com.tss.Model;

import java.util.Scanner;

public class ElectricityBill {

	private static int costPerUnit = 4;

	public static int getCostPerUnit() {
		return costPerUnit;
	}

	public static void setCostPerUnit(int costPerUnit) {
		ElectricityBill.costPerUnit = costPerUnit;
	}

	public static void changeCostperUnit() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Change the per unit - ");
		int unitChange = scanner.nextInt();
		setCostPerUnit(unitChange);
		System.out.println("Cost per unit updated to: " + costPerUnit);

	}

	public static void currenRate() {
		System.out.println("THe current rate is " + getCostPerUnit());
	}

	public static void Bill() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the unit Consumed \n");
		int unitConsumed = scanner.nextInt();
		int result = costPerUnit * unitConsumed;
		System.out.println("Total Amount to pay: " + result);
	}

	public static void guide() {
		System.out.println("Enter the 1 For getting the bill: ");
		System.out.println("Enter the 2 change Costper Unit");
		System.out.println("Enter the 3 for current rate");
		System.out.println("Enter the 4 for Exit");


	}

}
