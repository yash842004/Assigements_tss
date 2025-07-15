package com.tss.foodbill;

import java.util.List;

import com.tss.fooddelivery.menu.Menu;

public class CalculateBill extends Menu {

	public CalculateBill() {
		super();
	}

	public double getTotalBillForOrder(List<FoodItem> orderItems) {
		double total = 0;
		if (orderItems != null) {
			for (FoodItem item : orderItems) {
				if (item != null) {
					total += item.getPrice();
				}
			}
		}
		return total;
	}

}
