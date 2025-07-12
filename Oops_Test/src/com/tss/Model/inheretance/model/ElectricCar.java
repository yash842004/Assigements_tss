package com.tss.Model.inheretance.model;

public class ElectricCar extends Car {
	
	private int batteryLevel;
	
	
	public ElectricCar(String model, int year, int price, int numberOfDoors, int batteryLevel) {
		super(model, year, price, numberOfDoors); 
		this.batteryLevel = batteryLevel;
	}
	public void chargeBattery() {
		
		System.out.println("It is charging");
		
	}
	
	public void displayBatteryStatus() {
		System.out.println("the batteryLevel is "+batteryLevel);
	}

}
