package com.tss.fooddelivery.discount;

public class DiscountMonthly implements IDiscount {

	public double applyDiscount(double totalAmount) {
		double discount = 0;
		if (totalAmount > 500) {
			discount = totalAmount * 0.10;
			System.out.println("Bill-based discount of 10% applied: Rs. " + discount);
		} else {
			System.out.println("No bill-based discount applicable.");
		}
		return totalAmount - discount;
	}
}
