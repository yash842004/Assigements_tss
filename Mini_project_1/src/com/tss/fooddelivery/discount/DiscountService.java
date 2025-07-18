package com.tss.fooddelivery.discount;

import java.util.List;

public class DiscountService {

	private List<IDiscount> discounts;

	public DiscountService(List<IDiscount> discounts) {
		this.discounts = discounts;
	}

	public double applyAllDiscounts(double total) {
		for (IDiscount discount : discounts) {
			total = discount.applyDiscount(total);
		}
		return total;
	}
}
