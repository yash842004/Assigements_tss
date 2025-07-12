package com.tss.ecommers;

public class DefaultTaxCalculator implements TaxCalculator {

	public static final double TAX_PERCENT = 10.0;

	public double calculateTax(double cost) {
		return cost * TAX_PERCENT / 100;
	}
}
