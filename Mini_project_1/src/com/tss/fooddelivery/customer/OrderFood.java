package com.tss.fooddelivery.customer;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import com.tss.fooddelivery.foodbill.FoodItem;
import com.tss.fooddelivery.menu.Menu;
import com.tss.fooddelivery.foodbill.CalculateBill;
import com.tss.fooddelivery.Database.CustomerData;

public class OrderFood {

    private static int nextCustomerId = 1;

    public OrderResult placeOrder(Scanner scanner, Menu menu) {
        List<FoodItem> orderList = new ArrayList<>();
        System.out.println("\n--- Place Your Order ---");

        Address address = null;

        System.out.print("Are you an existing customer? yes/no: ");
        String existing = scanner.nextLine().trim().toLowerCase();

        if (existing.equals("yes")) {
            System.out.print("Enter your Customer ID: ");
            int enteredId = scanner.nextInt();
            scanner.nextLine(); 

            List<Address> customers = CustomerData.loadCustomers();
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

            List<FoodItem> selectedMenu = null;
            String cuisineName = "";

            switch (choice) {
                case 1:
                    selectedMenu = menu.getIndianMenuItems();
                    cuisineName = "Indian";
                    break;
                case 2:
                    selectedMenu = menu.getChineseMenuItems();
                    cuisineName = "Chinese";
                    break;
                case 3:
                    selectedMenu = menu.getItalianMenuItems();
                    cuisineName = "Italian";
                    break;
                default:
                    System.out.println("Invalid choice.");
                    continue;
            }

            System.out.println("\n--- " + cuisineName + " Menu ---");
            menu.displayMenu(cuisineName, selectedMenu);

            System.out.print("Enter Food Item ID to add to order: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            FoodItem item = null;
            for (FoodItem food : selectedMenu) {
                if (food.getId() == id) {
                    item = food;
                    break;
                }
            }

            if (item == null) {
                System.out.println("Invalid Food Item ID.");
                continue;
            }

            orderList.add(item);
            System.out.println(item.getName() + " added to your order.");
        }

        if (!orderList.isEmpty()) {
            CalculateBill bill = new CalculateBill();
            double totalBill = bill.getTotalBillForOrder(orderList);
        } else {
            System.out.println("No items ordered.");
        }


        return new OrderResult(orderList, address);
    }
}
