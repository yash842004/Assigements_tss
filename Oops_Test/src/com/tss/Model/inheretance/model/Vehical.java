package com.tss.Model.inheretance.model;

public class Vehical {
	
	private String model;
	private int year;
	private int price;

	public Vehical(String model, int year, int price) {
		
		this.model = model;
		this.year = year;
		this.price = price;
	}



	public void startEngine() {
		System.out.println("Engine is started");
	}

	public void stopEngine() {
		System.out.println("Engine is stoped");

	}

	public void displayInfo() {
		System.out.println("Model " + model);
		System.out.println("year " + year);
		System.out.println("price " + price);

	}

}
