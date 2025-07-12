package com.tss.Model.inheretance.model;

public class Car extends Vehical {

	private int numberOfDoors;
	


	public Car(String model, int year, int price, int numberOfDoors) {
		super(model, year, price);
		this.numberOfDoors = numberOfDoors;
	}

	public void lockDoors() {
		System.out.println("locked Doors");
	}

	public void unlockDoors() {
		System.out.println("unlocked Doors");

	}
	
	

}
