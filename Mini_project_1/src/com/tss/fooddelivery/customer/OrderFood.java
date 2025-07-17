package com.tss.fooddelivery.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.tss.fooddelivery.foodbill.CalculateBill;
import com.tss.fooddelivery.foodbill.FoodItem;
import com.tss.fooddelivery.menu.Menu;

public class OrderFood {

    private CalculateBill bill = new CalculateBill();

    public OrderResult placeOrder(Scanner scanner, Menu menu) {
        List<FoodItem> orderList = new ArrayList<>();
        System.out.println("\n--- Place Your Order ---");

        System.out.println("\nPlease enter your delivery address details.");
        System.out.print("City: ");
        String city = scanner.nextLine();

        System.out.print("State: ");
        String state = scanner.nextLine();

        System.out.print("Pincode: ");
        Long pincode = scanner.nextLong();

        Address address = new Address(city, state, pincode);
        System.out.println("Your delivery address is set to: " + address);

        Menu displayMenu = new Menu();

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
            displayMenu.displayMenu(cuisineName, selectedMenu);

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
            double totalBill = bill.getTotalBillForOrder(orderList);
            System.out.println("\nYour Total Bill before discount: Rs " + totalBill);
        } else {
            System.out.println("No items ordered.");
        }

        return new OrderResult(orderList, address);
    }
}
