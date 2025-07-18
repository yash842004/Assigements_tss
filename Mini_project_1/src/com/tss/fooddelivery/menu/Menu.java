package com.tss.fooddelivery.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tss.fooddelivery.foodbill.FoodItem;

public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<FoodItem> indianMenuItems = new ArrayList<>();
	private List<FoodItem> chineseMenuItems = new ArrayList<>();
	private List<FoodItem> italianMenuItems = new ArrayList<>();

	private int nextIndianId = 2;
	private int nextChineseId = 2;
	private int nextItalianId = 2;

	public Menu() {
		SampleMenus();
	}

	private void SampleMenus() {
		indianMenuItems.add(new FoodItem(1, "Chole Bhature", 120.0, "Main Course"));
		chineseMenuItems.add(new FoodItem(1, "Hakka Noodles", 150.0, "Main Course"));
		italianMenuItems.add(new FoodItem(1, "Margherita Pizza", 250.0, "Main Course"));
	}

	public void addFoodItemToIndianMenu(FoodItem item) {
		item.setId(nextIndianId++);
		indianMenuItems.add(item);
		System.out.println("Added '" + item.getName() + "' to Indian Menu.");
	}

	public void addFoodItemToChineseMenu(FoodItem item) {
		item.setId(nextChineseId++);
		chineseMenuItems.add(item);
		System.out.println("Added '" + item.getName() + "' to Chinese Menu.");
	}

	public void addFoodItemToItalianMenu(FoodItem item) {
		item.setId(nextItalianId++);
		italianMenuItems.add(item);
		System.out.println("Added '" + item.getName() + "' to Italian Menu.");
	}

	public void removeItemFromMenu(List<FoodItem> menuList, int idToRemove, String cuisine) {
		boolean removed = menuList.removeIf(item -> item.getId() == idToRemove);
		if (removed) {
			System.out.println("Item removed. Adjusting IDs...");
			adjustIds(menuList);
			resetNextId(menuList, cuisine);
		} else {
			System.out.println("Item ID not found. No removal done.");
		}
	}

	private void adjustIds(List<FoodItem> menuList) {
		int id = 1;
		for (FoodItem item : menuList) {
			item.setId(id++);
		}
	}

	private void resetNextId(List<FoodItem> menuList, String cuisine) {
		int nextId = menuList.size() + 1;
		switch (cuisine) {
		case "Indian" -> nextIndianId = nextId;
		case "Chinese" -> nextChineseId = nextId;
		case "Italian" -> nextItalianId = nextId;
		}
	}

	public List<FoodItem> getIndianMenuItems() {
		return indianMenuItems;
	}

	public List<FoodItem> getChineseMenuItems() {
		return chineseMenuItems;
	}

	public List<FoodItem> getItalianMenuItems() {
		return italianMenuItems;
	}

	public void displayMenu(String foodTypeName, List<FoodItem> menuItems) {
		System.out.println("\n--- Current " + foodTypeName + " Menu ---");
		System.out.printf("%-5s %-20s %-10s %-15s\n", "ID", "Name", "Price", "Category");
		System.out.println("------------------------------------------------------------");
		for (FoodItem item : menuItems) {
			System.out.printf("%-5d %-20s %-10.2f %-15s\n", item.getId(), item.getName(), item.getPrice(),
					item.getCategory());
		}
	}
}
