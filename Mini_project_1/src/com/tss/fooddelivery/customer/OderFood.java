package com.tss.fooddelivery.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.tss.foodbill.CalculateBill;
import com.tss.foodbill.FoodItem;
import com.tss.fooddelivery.menu.DisplayMenu;
import com.tss.fooddelivery.menu.Menu;

public class OderFood {

	private List<FoodItem> orderList = new ArrayList<>();
	private CalculateBill bill = new CalculateBill();

	public void placeOrder(Scanner scanner, Menu menu) {
		System.out.println("\n--- Place Your Order ---");

		DisplayMenu displayMenu = new DisplayMenu();

		boolean ordering = true;
		while (ordering) {
			System.out.println("\nSelect Cuisine to order from:");
			System.out.println("1. Indian");
			System.out.println("2. Chinese");
			System.out.println("3. Italian");
			System.out.println("4. Finish Ordering");
			System.out.print("Enter your choice: ");
			int choice = scanner.nextInt();
			scanner.nextLine(); 

			if (choice == 4) {
				ordering = false;
				break;
			}

			List<FoodItem> selectedMenu = switch (choice) {
			case 1 -> menu.getIndianMenuItems();
			case 2 -> menu.getChineseMenuItems();
			case 3 -> menu.getItalianMenuItems();
			default -> null;
			};

			if (selectedMenu != null) {
				System.out.println("\n--- " + getCuisineName(choice) + " Menu ---");
				displayMenu.displayMenu(selectedMenu);

				System.out.print("Enter Food Item ID to add to order: ");
				int id = scanner.nextInt();
				scanner.nextLine();

				FoodItem item = selectedMenu.stream().filter(f -> f.getId() == id).findFirst().orElse(null);

				if (item != null) {
					orderList.add(item);
					System.out.println(item.getName() + " added to your order.");
				} else {
					System.out.println("Invalid Food Item ID.");
				}
			} else {
				System.out.println("Invalid choice.");
			}
		}

		if (!orderList.isEmpty()) {
			bill.getTotalBillForOrder(orderList);
		} else {
			System.out.println("No items ordered.");
		}
	}

	private String getCuisineName(int choice) {
		return switch (choice) {
		case 1 -> "Indian";
		case 2 -> "Chinese";
		case 3 -> "Italian";
		default -> "Unknown";
		};
	}
}
