package com.tss.ecommers;

public class PercentageDiscountCalculator implements DiscountCalculator  {
	
	  @Override
	    public double calculateDiscount(double cost, double discountPercent) {
	        return cost * discountPercent / 100;
	    }

}
