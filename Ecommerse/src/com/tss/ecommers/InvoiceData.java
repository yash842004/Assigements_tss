package com.tss.ecommers;

public class InvoiceData {

	private int id;
	private String description;
	private double cost;
	private double discountPercent;

	public InvoiceData(int id, String description, double cost, double discountPercent) {
		this.id = id;
		this.description = description;
		this.cost = cost;
		this.discountPercent = discountPercent;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public double getCost() {
		return cost;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}
}
