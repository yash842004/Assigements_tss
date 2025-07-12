package com.tss.ecommers; // Ensure this matches the LineItem's package

public class Product {
	private int id;
	private String name;
	private double price;
	private double discountPercent;

	public Product(int id, String name, double price, double discountPercent) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.discountPercent = discountPercent;
	}

	public int getId() {
		return id;
	}

	public String getName() { 
		return name;
	}

	public double getPrice() {
		return price;
	}

	public double getDiscountPercent() {
		return discountPercent;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setDiscountPercent(double discountPercent) {
		this.discountPercent = discountPercent;
	}


	public double calculateDiscountedPrice() {
		return price * (1 - discountPercent / 100.0);
	}

	@Override
	public String toString() {
		return "Product{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price + ", discountPercent="
				+ discountPercent + '}';
	}
}