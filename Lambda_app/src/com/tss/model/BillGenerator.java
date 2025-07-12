package com.tss.model;

public class BillGenerator {
	
	public BillGenerator(String name, int price) {
		super();
		this.name = name;
		this.price = price;
	}
	private String name;
	private int price;
	private int finalAmount;
	
	
	private int tax = 18;
	private int shipping = 20;
	private String itemName;
	private double basePrice;


    public BillGenerator(String itemName, double basePrice) {
        this.itemName = itemName;
        this.basePrice = basePrice;
    }
	 public String generateBill() {
	        double tax = basePrice * TAX_RATE;
	        double finalAmount = basePrice + tax + SHIPPING_CHARGE;


	    }
}
