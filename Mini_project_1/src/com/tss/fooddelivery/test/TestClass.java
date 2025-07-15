package com.tss.fooddelivery.test;

import java.util.Scanner;

import com.tss.fooddelivery.admin.Admin;
import com.tss.fooddelivery.customer.OderFood;
import com.tss.fooddelivery.menu.Menu;

import com.tss.fooddeliverycom.payments.Payment;

public class TestClass {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		Menu menu = new Menu();
		Admin admin = new Admin();
		OderFood oderFood = new OderFood();
		Payment payment = new Payment();

		while (true) {
			System.out.println("\n====== Food Delivery Application ======");
			System.out.println("1. Customer");
			System.out.println("2. Admin Login");
			System.out.println("3. Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
			case 1:
				boolean customerMenu = true;
				while (customerMenu) {
					System.out.println("\n--- Customer Panel ---");
					System.out.println("1. View Menu");
					System.out.println("2. Place Order");
					System.out.println("3. Back to Main Menu");
					System.out.println("4. Choices payment");
					System.out.print("Enter your choice: ");
					int custChoice = scanner.nextInt();
					scanner.nextLine();

					switch (custChoice) {
					case 1:
						System.out.println("\nSelect Cuisine to view:");
						System.out.println("1. Indian");
						System.out.println("2. Chinese");
						System.out.println("3. Italian");
						System.out.print("Enter your choice: ");
						int menuChoice = scanner.nextInt();
						scanner.nextLine();

						switch (menuChoice) {
						case 1:
							menu.displayCuisineMenu("Indian", menu.getIndianMenuItems());
							break;
						case 2:
							menu.displayCuisineMenu("Chinese", menu.getChineseMenuItems());
							break;
						case 3:
							menu.displayCuisineMenu("Italian", menu.getItalianMenuItems());
							break;

						}
						break;

					case 2:
						oderFood.placeOrder(scanner, menu);
						break;

					case 3:
						customerMenu = false;
						break;
					case 4:
						System.out.println("1. for Cash payment");
						System.out.println("2. for Upi payment");
						System.out.println("3. for Credit card payment");

						int paymentChoice = scanner.nextInt();
						switch (paymentChoice) {

						case 1:
							payment.cashPay();
							break;
						case 2:
							payment.UPIPay();
							break;
						case 3:
							payment.creditCardPay();
							break;

						}

					default:
						System.out.println("Invalid choice. Try again.");
					}
				}
				break;

			case 2:
				if (admin.verify()) {
					boolean adminMenu = true;
					while (adminMenu) {
						System.out.println("\n--- Admin Panel ---");
						System.out.println("1. Add Food Item");
						System.out.println("2. Remove Food Item");
						System.out.println("3. Edit Food Item");
						System.out.println("4. Manage Discounts");
						System.out.println("5. View All Menus");
						System.out.println("6. Logout");
						System.out.print("Enter your choice: ");

						int adminChoice = scanner.nextInt();
						scanner.nextLine();

						switch (adminChoice) {
						case 1:
							admin.addItems(scanner, menu);
							break;
						case 2:
							admin.removeItems(scanner, menu);
							break;
						case 3:
							admin.editItems(scanner, menu);
							break;
						case 4:
							admin.manageDiscounts(scanner, menu);
							break;
						case 5:
							admin.viewCurrentMenus(menu);
							break;
						case 6:
							adminMenu = false;
							System.out.println("Logged out of Admin Panel.");
							break;
						default:
							System.out.println("Invalid choice. Try again.");
						}
					}
				}
				break;

			case 3:
				System.out.println("Thank you for using Food Delivery Application.");
				scanner.close();
				System.exit(0);
				break;

			default:
				System.out.println("Invalid choice. Try again.");
			}
		}
	}
}
