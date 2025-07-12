package com.tss.ecommers;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
	private List<LineItem> lineItems;

	public ShoppingCart() {
		this.lineItems = new ArrayList<>();
	}

	public void addLineItem(LineItem item) {
		if (item != null) {
			this.lineItems.add(item);
			System.out.println("Added to cart: " + item.getProduct().getName() + " x " + item.getQuantity());
		}
	}

	public boolean removeLineItem(int lineItemId) {
		return lineItems.removeIf(item -> item.getId() == lineItemId);
	}

	public double getTotalCartCost() {
		double total = 0;
		for (LineItem item : lineItems) {
			total += item.calculateLineItemCost();
		}
		return total;
	}

	public void displayCartContents() {
		System.out.println("\n--- Shopping Cart Contents ---");
		System.out.printf("%-5s %-20s %-10s %-12s %-12s\n", "ID", "Name", "Quantity", "UnitPrice", "Total");
		System.out.println("------------------------------------------------------------------");

		for (LineItem item : lineItems) {
			String productName = (item.getProduct() != null) ? item.getProduct().getName() : "N/A";
			System.out.printf("%-5d %-20s %-10d %-12.2f %-12.2f\n", item.getId(), productName, item.getQuantity(),
					item.getUnitPrice(), item.calculateLineItemCost());
		}
		System.out.println("------------------------------------------------------------------");
		System.out.printf("%-49s %-12.2f\n", "Total Cart Cost:", getTotalCartCost());
		System.out.println("------------------------------------------------------------------");
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}
}
