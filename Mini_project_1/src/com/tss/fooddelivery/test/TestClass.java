package com.tss.fooddelivery.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.tss.fooddelivery.Database.MenuData;
import com.tss.fooddelivery.admin.Admin;
import com.tss.fooddelivery.customer.Address;
import com.tss.fooddelivery.customer.Cart;
import com.tss.fooddelivery.customer.OrderFood;
import com.tss.fooddelivery.discount.DiscountMonthly;
import com.tss.fooddelivery.discount.DiscountService;
import com.tss.fooddelivery.discount.FestivalDiscount;
import com.tss.fooddelivery.discount.IDiscount;
import com.tss.fooddelivery.foodbill.CalculateBill;
import com.tss.fooddelivery.foodpatner.DeliveryPartnerService;
import com.tss.fooddelivery.foodpatner.IDeliveryPartnerService;
import com.tss.fooddelivery.menu.Menu;
import com.tss.fooddelivery.payments.Payment;

public class TestClass {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		Menu menu = MenuData.loadMenu();

		IDeliveryPartnerService deliveryService = new DeliveryPartnerService();
		Admin admin = new Admin(deliveryService);
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
				Cart cart = new Cart();
				Address address = orderFood.getDeliveryAddress(scanner);

				while (customerMenu) {
					System.out.println("\n--- Customer Panel ---");
					System.out.println("1. View Menu");
					System.out.println("2. Manage Cart and Place Order");
					System.out.println("3. Back to Main Menu");
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
						case 1 -> menu.displayMenu("Indian", menu.getIndianMenuItems());
						case 2 -> menu.displayMenu("Chinese", menu.getChineseMenuItems());
						case 3 -> menu.displayMenu("Italian", menu.getItalianMenuItems());
						default -> System.out.println("Invalid cuisine choice.");
						}
						break;

					case 2:
						orderFood.placeOrder(scanner, menu, cart);

						if (!cart.isEmpty()) {
							CalculateBill calculateBill = new CalculateBill();
							double totalBill = calculateBill.getTotalBillForOrder(cart.getItems());

							List<IDiscount> discountList = new ArrayList<>();

							if (totalBill > 500) {
								System.out
										.println("Monthly discount applied automatically as bill is greater than 500.");
								discountList.add(new DiscountMonthly());
							}

							System.out.print("Apply festival discount? ('yes' or 'no'): ");
							if (scanner.nextLine().equalsIgnoreCase("yes")) {
								discountList.add(new FestivalDiscount());
							}

							DiscountService discountService = new DiscountService(discountList);
							double finalBill = discountService.applyAllDiscounts(totalBill);

							System.out.println("Your Final Bill after all applicable discounts: Rs. " + finalBill);

							System.out
									.println("Payment Time :) -> ('yes' for online payment & 'no' for cash payment): ");
							String payChoice = scanner.nextLine();

							if (payChoice.equalsIgnoreCase("yes")) {
								System.out.println("Choose payment method:");
								System.out.println("1. UPI Payment");
								System.out.println("2. Credit Card Payment");

								int paymentChoice = scanner.nextInt();
								scanner.nextLine();

								switch (paymentChoice) {
								case 1 -> payment.UPIPay();
								case 2 -> payment.creditCardPay();
								default -> {
									System.out.println("Invalid payment choice. Defaulting to cash on delivery.");
									payment.cashPay();
								}
								}

							} else {
								System.out.println("You chose to pay on delivery.");
								payment.cashPay();
							}

							deliveryService.deliverOrder(address);

							System.out.println("Delivery process completed. Exiting application.");

							MenuData.saveMenu(menu);

							scanner.close();
							System.exit(0);

						} else {
							System.out.println("Your cart is empty. Please add items before placing order.");
						}
						break;

					case 3:
						customerMenu = false;
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
						System.out.println("6. Add Delivery Partner");
						System.out.println("7. View Delivery Partners");
						System.out.println("8. Logout");
						System.out.print("Enter your choice: ");

						int adminChoice = scanner.nextInt();
						scanner.nextLine();

						switch (adminChoice) {
						case 1 -> admin.addItems(scanner, menu);
						case 2 -> admin.removeItems(scanner, menu);
						case 3 -> admin.editItems(scanner, menu);
						case 4 -> admin.manageDiscounts(scanner, menu);
						case 5 -> admin.viewCurrentMenus(menu);
						case 6 -> admin.addDeliveryPartner(scanner);
						case 7 -> admin.viewDeliveryPartners();
						case 8 -> {
							adminMenu = false;
							System.out.println("Logged out of Admin Panel.");
						}
						default -> System.out.println("Invalid choice. Try again.");
						}
					}

					MenuData.saveMenu(menu);
				}
				break;

			case 3:
				System.out.println("Thank you for using Food Delivery Application.");

				MenuData.saveMenu(menu);

				scanner.close();
				System.exit(0);
				break;

			default:
				System.out.println("Invalid choice. Try again.");
			}
		}
	}
}
