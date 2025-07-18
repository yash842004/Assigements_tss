package com.tss.fooddelivery.discount;

public class FestivalDiscount implements IDiscount {

	private double festivalDiscountRate = 0.20;

	@Override
	public double applyDiscount(double totalAmount) {
		double discount = totalAmount * festivalDiscountRate;
		System.out.println("Festival discount of 20% applied: " + discount);
		return totalAmount - discount;
	}

}
