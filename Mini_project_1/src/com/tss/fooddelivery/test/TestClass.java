package com.tss.fooddelivery.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.tss.fooddelivery.admin.Admin;
import com.tss.fooddelivery.customer.OrderFood;
import com.tss.fooddelivery.discount.DiscountMonthly;
import com.tss.fooddelivery.discount.DiscountService;
import com.tss.fooddelivery.discount.FestivalDiscount;
import com.tss.fooddelivery.discount.IDiscount;
import com.tss.fooddelivery.foodbill.CalculateBill;
import com.tss.fooddelivery.foodbill.FoodItem;
import com.tss.fooddelivery.menu.Menu;
import com.tss.fooddelivery.payments.Payment;

public class TestClass {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		Menu menu = new Menu();
		Admin admin = new Admin();
		OrderFood orderFood = new OrderFood();
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
					System.out.println("4. Payment Options");
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
							menu.displayMenu("Indian", menu.getIndianMenuItems());
							break;
						case 2:
							menu.displayMenu("Chinese", menu.getChineseMenuItems());
							break;
						case 3:
							menu.displayMenu("Italian", menu.getItalianMenuItems());
							break;
						default:
							System.out.println("Invalid cuisine choice.");
						}
						break;

					case 2:
						List<FoodItem> orderItems = orderFood.placeOrder(scanner, menu);

						if (orderItems != null && !orderItems.isEmpty()) {
							CalculateBill calculateBill = new CalculateBill();
							double totalBill = calculateBill.getTotalBillForOrder(orderItems);

							// Apply discounts
							List<IDiscount> discountList = new ArrayList<>();

							if (totalBill > 500) {
								System.out.println("Monthly discount applied automatically as bill is greater than 500.");
								discountList.add(new DiscountMonthly());
							}

							System.out.print("Apply festival discount? (yes/no): ");
							if (scanner.nextLine().equalsIgnoreCase("yes")) {
								discountList.add(new FestivalDiscount());
							}

							DiscountService discountService = new DiscountService(discountList);
							double finalBill = discountService.applyAllDiscounts(totalBill);

							System.out.println("Your Final Bill after all applicable discounts: $" + finalBill);

							System.out.print("Proceed to payment? (yes/no): ");
							String payChoice = scanner.nextLine();
							if (payChoice.equalsIgnoreCase("yes")) {
								System.out.println("1. Cash Payment");
								System.out.println("2. UPI Payment");
								System.out.println("3. Credit Card Payment");

								int paymentChoice = scanner.nextInt();
								scanner.nextLine();

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
								default:
									System.out.println("Invalid payment choice.");
								}

								System.out.println("Payment completed. Thank you for using Food Delivery Application.");
								scanner.close();
								System.exit(0);

							} else {
								System.out.println("Order placed without payment. Please pay on delivery!");
								System.out.println("Thank you for ordering. Exiting application...");
								scanner.close();
								System.exit(0);
							}

						} else {
							System.out.println("No items ordered.");
						}
						break;

					case 3:
						customerMenu = false;
						break;

					case 4:
						System.out.println("1. Cash Payment");
						System.out.println("2. UPI Payment");
						System.out.println("3. Credit Card Payment");

						int paymentChoice = scanner.nextInt();
						scanner.nextLine();

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
						default:
							System.out.println("Invalid payment choice.");
						}
						break;

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
