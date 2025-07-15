package com.tss.fooddelivery.menu;

import java.util.List;

import com.tss.foodbill.FoodItem;

public class DisplayMenu {

    public  void displayMenu(List<FoodItem> menuItems) {
        for (FoodItem item : menuItems) {
            System.out.printf("%d. %s - Rs. %.2f (%s)%n",
                item.getId(),
                item.getName(),
                (double) item.getPrice(),
                item.getCategory()
            );
        }
    }
}
