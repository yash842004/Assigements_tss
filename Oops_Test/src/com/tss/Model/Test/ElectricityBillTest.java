package com.tss.Model.Test;

import java.util.Scanner;

import com.tss.Model.ElectricityBill;

public class ElectricityBillTest {

	public static void main(String[] args) {

		ElectricityBill bill = new ElectricityBill();

		Scanner scanner = new Scanner(System.in);

		boolean condition = true;
		while (condition) {
			System.out.println("The bill of Apartment- 1");
			System.out.println();
			ElectricityBill.guide();
			System.out.println();
			System.out.println("Enter the case: ");
			int number = scanner.nextInt();
			switch (number) {

			case 1: {
				ElectricityBill.Bill();
				break;
			}
			case 2: {
				ElectricityBill.changeCostperUnit();
				break;
			}
			case 3: {
				ElectricityBill.currenRate();
				break;
			}
			case 4: {
				System.out.println("Exit");
				condition = false;
				break;
			}
			}
		}
	}

}
