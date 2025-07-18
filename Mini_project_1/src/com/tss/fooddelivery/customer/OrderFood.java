package com.tss.fooddelivery.customer;

import java.util.Scanner;
import com.tss.fooddelivery.foodbill.FoodItem;
import com.tss.fooddelivery.menu.Menu;
import com.tss.fooddelivery.foodbill.CalculateBill;
import com.tss.fooddelivery.Database.CustomerData;

public class OrderFood {

    private static int nextCustomerId = 1;

    public void placeOrder(Scanner scanner, Menu menu, Cart cart) {
        System.out.println("\n--- Place Your Order ---");

        boolean ordering = true;
        while (ordering) {
            System.out.println("\n--- Cart Menu ---");
            System.out.println("1. Add Item to Cart");
            System.out.println("2. Remove Item from Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Finish Ordering");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\nSelect Cuisine to order from:");
                    System.out.println("1. Indian");
                    System.out.println("2. Chinese");
                    System.out.println("3. Italian");
                    System.out.print("Enter your choice: ");
                    int cuisineChoice = scanner.nextInt();
                    scanner.nextLine();

                    var selectedMenu = switch (cuisineChoice) {
                        case 1 -> menu.getIndianMenuItems();
                        case 2 -> menu.getChineseMenuItems();
                        case 3 -> menu.getItalianMenuItems();
                        default -> {
                            System.out.println("Invalid cuisine choice.");
                            yield null;
                        }
                    };

                    if (selectedMenu != null) {
                        System.out.println("\n--- Menu ---");
                        menu.displayMenu("", selectedMenu);

                        System.out.print("Enter Food Item ID to add to cart: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();

                        FoodItem item = null;
                        for (FoodItem food : selectedMenu) {
                            if (food.getId() == id) {
                                item = food;
                                break;
                            }
                        }

                        if (item != null) {
                            cart.addItem(item);
                        } else {
                            System.out.println("Invalid Food Item ID.");
                        }
                    }
                    break;

                case 2:
                    cart.displayCart();
                    System.out.print("Enter Food Item ID to remove from cart: ");
                    int removeId = scanner.nextInt();
                    scanner.nextLine();
                    cart.removeItem(removeId);
                    break;

                case 3:
                    cart.displayCart();
                    break;

                case 4:
                    ordering = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        if (!cart.isEmpty()) {
            CalculateBill bill = new CalculateBill();
            double totalBill = bill.getTotalBillForOrder(cart.getItems());
            System.out.println("Your total bill (before discounts): Rs. " + totalBill);
        } else {
            System.out.println("No items ordered.");
        }
    }

    public Address getDeliveryAddress(Scanner scanner) {
        System.out.print("Are you an existing customer? yes/no: ");
        String existing = scanner.nextLine().trim().toLowerCase();

        Address address = null;

        if (existing.equals("yes")) {
            System.out.print("Enter your Customer ID: ");
            int enteredId = scanner.nextInt();
            scanner.nextLine();

            var customers = CustomerData.loadCustomers();
            for (Address customer : customers) {
                if (customer.getId() == enteredId) {
                    address = customer;
                    System.out.println("Welcome back! Your details: " + address);
                    break;
                }
            }

            if (address == null) {
                System.out.println("Customer ID not found. Registering as new customer.");
            }
        }

        if (address == null) {
            System.out.println("\n Enter your Name");
            String name = scanner.nextLine();
            System.out.println("\nPlease enter your delivery address details.");
            System.out.print("City: ");
            String city = scanner.nextLine();

            System.out.print("State: ");
            String state = scanner.nextLine();

            System.out.print("Pincode: ");
            Long pincode = scanner.nextLong();
            scanner.nextLine();

            int customerId = nextCustomerId++;

            address = new Address(customerId, name, city, state, pincode);
            System.out.println("Your delivery address is set to: " + address);
            System.out.println("Your generated Customer ID is: " + customerId);

            CustomerData.saveCustomer(address);
        }

        return address;
    }
}
