package com.tss.fooddelivery.customer;

import java.util.ArrayList;
import java.util.List;
import com.tss.fooddelivery.foodbill.FoodItem;

public class Cart {

    private List<FoodItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public void addItem(FoodItem item) {
        items.add(item);
        System.out.println(item.getName() + " added to your cart.");
    }

    public void removeItem(int id) {
        FoodItem toRemove = null;
        for (FoodItem item : items) {
            if (item.getId() == id) {
                toRemove = item;
                break;
            }
        }
        if (toRemove != null) {
            items.remove(toRemove);
            System.out.println(toRemove.getName() + " removed from your cart.");
        } else {
            System.out.println("Item with ID " + id + " not found in your cart.");
        }
    }

    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("\n--- Your Cart ---");
            for (FoodItem item : items) {
                System.out.println("ID: " + item.getId() + " | Name: " + item.getName() + " | Price: " + item.getPrice());
            }
        }
    }

    public List<FoodItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
