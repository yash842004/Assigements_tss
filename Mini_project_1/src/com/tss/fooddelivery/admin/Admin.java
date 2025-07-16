package com.tss.fooddelivery.admin;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.tss.fooddelivery.foodbill.FoodItem;
import com.tss.fooddelivery.menu.Menu;

public class Admin extends Menu {

	private String correctUserName = "adminUser";
	private String correctPassword = "admin123@";

	public boolean verify() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter username: ");
		String userName = scanner.next();
		System.out.print("Enter password: ");
		String password = scanner.next();

		if (correctUserName.equals(userName) && correctPassword.equals(password)) {
			System.out.println("Login Successful!");
			return true;
		} else {
			System.out.println("Login Failed. Invalid username or password.");
			return false;
		}
	}

	public void addItems(Scanner scanner, Menu menu) {
		System.out.println("\n--- Add New Food Item ---");

		int id = inputInt(scanner, "Enter Food Item ID: ");
		System.out.print("Enter Food Item Name: ");
		String name = scanner.nextLine();

		double price = inputDouble(scanner, "Enter Food Item Price: ");
		System.out.print("Enter Food Item Category (e.g., Starter, Main Course): ");
		String category = scanner.nextLine();

		FoodItem newItem = new FoodItem(id, name, price, category);

		int choice = selectFoodType(scanner);
		switch (choice) {
		case 1 -> {
			menu.addFoodItemToIndianMenu(newItem);
			System.out.println("Item added to Indian menu.");
		}
		case 2 -> {
			menu.addFoodItemToChineseMenu(newItem);
			System.out.println("Item added to Chinese menu.");
		}
		case 3 -> {
			menu.addFoodItemToItalianMenu(newItem);
			System.out.println("Item added to Italian menu.");
		}
		}
	}

	public void viewCurrentMenus(Menu menu) {
		System.out.println("\n--- Viewing All Menus ---");
		menu.displayMenu("Indian", menu.getIndianMenuItems());
		menu.displayMenu("Chinese", menu.getChineseMenuItems());
		menu.displayMenu("Italian", menu.getItalianMenuItems());
	}

	public void removeItems(Scanner scanner, Menu menu) {
		System.out.println("\n--- Remove Food Item ---");

		int choice = selectFoodType(scanner);
		List<FoodItem> selectedMenu = getMenuList(menu, choice);
		String cuisineName = getFoodTypes(choice);

		if (selectedMenu.isEmpty()) {
			System.out.println(cuisineName + " menu is empty. Nothing to remove.");
			return;
		}

		menu.displayMenu(cuisineName, selectedMenu);
		int idToRemove = inputInt(scanner, "Enter Food Item ID to remove: ");

		boolean removed = selectedMenu.removeIf(item -> item.getId() == idToRemove);
		if (removed) {
			System.out.println("Item removed from " + cuisineName + " menu.");
		} else {
			System.out.println("Item ID not found in " + cuisineName + " menu.");
		}
	}

	public void editItems(Scanner scanner, Menu menu) {
		System.out.println("\n--- Edit Food Item ---");

		int cuisineChoice = selectFoodType(scanner);
		List<FoodItem> selectedMenu = getMenuList(menu, cuisineChoice);
		String cuisineName = getFoodTypes(cuisineChoice);

		if (selectedMenu.isEmpty()) {
			System.out.println(cuisineName + " menu is empty. Nothing to edit.");
			return;
		}

		menu.displayMenu(cuisineName, selectedMenu);
		int idToEdit = inputInt(scanner, "Enter Food Item ID to edit: ");

		FoodItem itemToEdit = selectedMenu.stream().filter(item -> item.getId() == idToEdit).findFirst().orElse(null);

		if (itemToEdit != null) {
			System.out.print("Enter new Name (Enter to keep '" + itemToEdit.getName() + "'): ");
			String newName = scanner.nextLine();
			if (!newName.isEmpty())
				itemToEdit.setName(newName);

			System.out.print("Enter new Price (Enter to keep " + itemToEdit.getPrice() + "): ");
			String priceInput = scanner.nextLine();
			if (!priceInput.isEmpty())
				itemToEdit.setPrice(Integer.parseInt(priceInput));

			System.out.print("Enter new Category (Enter to keep '" + itemToEdit.getCategory() + "'): ");
			String newCategory = scanner.nextLine();
			if (!newCategory.isEmpty())
				itemToEdit.setCategory(newCategory);

			System.out.println("Item updated successfully.");
		} else {
			System.out.println("Item ID not found in " + cuisineName + " menu.");
		}
	}

	public void manageDiscounts(Scanner scanner, Menu menu) {
		System.out.println("\n--- Manage Discounts ---");

		int cuisineChoice = selectFoodType(scanner);
		List<FoodItem> selectedMenu = getMenuList(menu, cuisineChoice);
		String cuisineName = getFoodTypes(cuisineChoice);

		if (selectedMenu.isEmpty()) {
			System.out.println(cuisineName + " menu is empty. No discounts to manage.");
			return;
		}

		menu.displayMenu(cuisineName, selectedMenu);
		int idForDiscount = inputInt(scanner, "Enter Food Item ID to manage discount: ");

		FoodItem item = selectedMenu.stream().filter(f -> f.getId() == idForDiscount).findFirst().orElse(null);

		if (item != null) {
			System.out.println("1. Apply new discount");
			System.out.println("2. Remove discount");
			System.out.print("Enter choice: ");
			int choice = scanner.nextInt();
			scanner.nextLine();

			if (choice == 1) {
				int discount = inputInt(scanner, "Enter discount percentage: ");
				item.setDiscountPercentage(discount);
				System.out.println("Discount applied to " + item.getName());
			} else if (choice == 2) {
				item.setDiscountPercentage(0);
				System.out.println("Discount removed from " + item.getName());
			} else {
				System.out.println("Invalid choice.");
			}
		} else {
			System.out.println("Item ID not found in " + cuisineName + " menu.");
		}
	}

	private int selectFoodType(Scanner scanner) {
		System.out.println("Select selectFoodType:");
		System.out.println("1. Indian");
		System.out.println("2. Chinese");
		System.out.println("3. Italian");
		System.out.print("Enter your choice: ");
		return scanner.nextInt();
	}

	private List<FoodItem> getMenuList(Menu menu, int cuisineChoice) {
		return switch (cuisineChoice) {
		case 1 -> menu.getIndianMenuItems();
		case 2 -> menu.getChineseMenuItems();
		case 3 -> menu.getItalianMenuItems();
		default -> throw new IllegalStateException("Invalid cuisine choice");
		};
	}

	private String getFoodTypes(int choice) {
		return switch (choice) {
		case 1 -> "Indian";
		case 2 -> "Chinese";
		case 3 -> "Italian";
		default -> "Unknown";
		};
	}

	private int inputInt(Scanner scanner, String prompt) {
		int value;
		while (true) {
			System.out.print(prompt);
			try {
				value = scanner.nextInt();
				scanner.nextLine(); 
				if (value > 0)
					break;
				else
					System.out.println("Value must be positive.");
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Enter a number.");
				scanner.nextLine(); 
			}
		}
		return value;
	}

	private int inputDouble(Scanner scanner, String prompt) {
		int value;
		while (true) {
			System.out.print(prompt);
			try {
				value = scanner.nextInt();
				scanner.nextLine();
				if (value > 0)
					break;
				else
					System.out.println("Value must be positive.");
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Enter a decimal number.");
				scanner.nextLine();
			}
		}
		return value;
	}
}
