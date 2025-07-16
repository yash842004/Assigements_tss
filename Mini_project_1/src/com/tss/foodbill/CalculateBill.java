package com.tss.foodbill;

import java.util.List;

public class CalculateBill {

    public double getTotalBillForOrder(List<FoodItem> orderItems) {
        double total = 0;
        if (orderItems != null) {
            for (FoodItem item : orderItems) {
                if (item != null) {
                    total += item.getPrice();
                }
            }
        }

        System.out.println("Your Total Bill before discount: Rs " + total);
        return total;
    }
}
