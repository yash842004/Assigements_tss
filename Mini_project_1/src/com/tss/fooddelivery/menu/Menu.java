package com.tss.fooddelivery.menu;

import java.util.ArrayList;
import java.util.List;

import com.tss.foodbill.FoodItem;

public class Menu {
    private List<FoodItem> indianMenuItems = new ArrayList<>();
    private List<FoodItem> chineseMenuItems = new ArrayList<>();
    private List<FoodItem> italianMenuItems = new ArrayList<>();

    public Menu() {
        loadSampleMenus();
    }

    private void loadSampleMenus() {
        indianMenuItems.add(new FoodItem(1, "Chole Bhature", 120.0, "Main Course"));
        chineseMenuItems.add(new FoodItem(2, "Hakka Noodles", 150.0, "Main Course"));
        italianMenuItems.add(new FoodItem(3, "Margherita Pizza", 250.0, "Main Course"));
        System.out.println("Sample menus loaded.");
    }

    public void addFoodItemToIndianMenu(FoodItem item) {
        indianMenuItems.add(item);
        System.out.println("Added '" + item.getName() + "' to Indian Menu.");
    }

    public void addFoodItemToChineseMenu(FoodItem item) {
        chineseMenuItems.add(item);
        System.out.println("Added '" + item.getName() + "' to Chinese Menu.");
    }

    public void addFoodItemToItalianMenu(FoodItem item) {
        italianMenuItems.add(item);
        System.out.println("Added '" + item.getName() + "' to Italian Menu.");
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

    public void displayCuisineMenu(String cuisineName, List<FoodItem> menuItems) {
        System.out.println("\n--- Current " + cuisineName + " Menu ---");
        System.out.printf("%-5s %-20s %-10s %-15s\n", "ID", "Name", "Price", "Category");
        System.out.println("------------------------------------------------------------");
        for (FoodItem item : menuItems) {
            System.out.printf("%-5d %-20s %-10.2f %-15s\n",
                    item.getId(), item.getName(), item.getPrice(), item.getCategory());
        }
    }
}
