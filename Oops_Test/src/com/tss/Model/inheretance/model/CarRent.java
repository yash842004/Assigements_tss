package com.tss.Model.inheretance.model;

import java.util.Scanner;

public class CarRent extends VehicalRental {

	public int getRentperDay() {
		return rentperDay;
	}

	public void setRentperDay(int rentperDay) {
		this.rentperDay = rentperDay;
	}

	private int rentperDay;

	public CarRent(String vehicalNumber, int rent, String fuelType) {
		super(vehicalNumber, rent, fuelType);
		// TODO Auto-generated constructor stub
	}

	public void rentCalculate() {
		Scanner scanner = new Scanner(System.in);
		setRentperDay(100);
		System.out.println("Enter the number of days for cars");
		int numberOfDays = scanner.nextInt();
		int result = numberOfDays * getRentperDay();
		System.out.println(result);
	}
	public void display() {
		System.out.println("rent of car is : "+ getRentperDay());
	}

}
