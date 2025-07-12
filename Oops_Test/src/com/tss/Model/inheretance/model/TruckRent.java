package com.tss.Model.inheretance.model;

import java.util.Scanner;

public class TruckRent extends VehicalRental{

	private int rentperDay;
	public int getRentperDay() {
		return rentperDay;
	}

	public void setRentperDay(int rentperDay) {
		this.rentperDay = rentperDay;
	}

	public TruckRent(String vehicalNumber, int rent, String fuelType) {
		super(vehicalNumber, rent, fuelType);
	}

	public void rentCalculate() {
		Scanner scanner = new Scanner(System.in);
		setRentperDay(100);
		System.out.println("Enter the number of days for track");
		int numberOfDays = scanner.nextInt();
		int result = (numberOfDays * getRentperDay())+500;
		System.out.println(result);
		
	}
	
	public void display() {
		System.out.println("rent of truck is : "+ getRentperDay());
	}
}
