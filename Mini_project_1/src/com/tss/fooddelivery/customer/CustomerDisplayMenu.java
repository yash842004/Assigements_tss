package com.tss.fooddelivery.customer;

import java.util.ArrayList;
import java.util.List;

import com.tss.foodbill.FoodItem;

public class CustomerDisplayMenu {

	private List<FoodItem> orderList = new ArrayList<>();

	public class DisplayMenu {
		public void displayCuisineMenu(String cuisineName, List<FoodItem> items) {
			System.out.println("\n--- " + cuisineName + " Menu ---");
			if (items.isEmpty()) {
				System.out.println("No items available in " + cuisineName + " menu.");
			} else {
				System.out.printf("%-5s %-20s %-10s %-15s %-10s\n", "ID", "Name", "Price", "Category", "Discount");
				for (FoodItem item : items) {
					System.out.printf("%-5d %-20s %-10.2f %-15s %-10s\n", item.getId(), item.getName(),
							(double) item.getPrice(), item.getCategory(), item.getDiscountPercentage() + "%");
				}
			}
		}
	}

}
