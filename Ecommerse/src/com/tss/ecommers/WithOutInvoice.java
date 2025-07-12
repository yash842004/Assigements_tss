package com.tss.ecommers;

public class WithOutInvoice {
	private InvoiceData data;
	private TaxCalculator taxCalculator;
	private DiscountCalculator discountCalculator;

	public WithOutInvoice(InvoiceData data, TaxCalculator taxCalculator, DiscountCalculator discountCalculator) {
		this.data = data;
		this.taxCalculator = taxCalculator;
		this.discountCalculator = discountCalculator;
	}

	public double calculateFinalAmount() {
		double tax = taxCalculator.calculateTax(data.getCost());
		double discount = discountCalculator.calculateDiscount(data.getCost(), data.getDiscountPercent());
		return data.getCost() + tax - discount;
	}

	public InvoiceData getData() {
		return data;
	}
}
