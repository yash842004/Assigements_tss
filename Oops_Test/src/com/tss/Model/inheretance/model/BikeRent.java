package com.tss.Model.inheretance.model;

import java.util.Scanner;

public class BikeRent extends VehicalRental {
	public BikeRent(String vehicalNumber, int rent, String fuelType) {
		super(vehicalNumber, rent, fuelType);
		// TODO Auto-generated constructor stub
	}
	private int rentperDay;


	public int getRentperDay() {
		return rentperDay;
	}


	public void setRentperDay(int rentperDay) {
		this.rentperDay = rentperDay;
	}


	public void rentCalculate() {
		Scanner scanner = new Scanner(System.in);
		setRentperDay(100);
		System.out.println("Enter the number of days for Bike: ");
		int numberOfDays = scanner.nextInt();
		int  result = numberOfDays * getRentperDay();
		if (numberOfDays >= 5) {
			 int discount = (result * 10) / 100;
			 result = result - discount;
		}
		System.out.println(result);

	}
	public void display() {
		System.out.println("rent of bike is : "+ getRentperDay());
	}
	

}
