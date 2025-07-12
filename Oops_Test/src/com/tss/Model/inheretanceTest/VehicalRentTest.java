package com.tss.Model.inheretanceTest;

import java.util.Scanner;

import com.tss.Model.inheretance.model.BikeRent;
import com.tss.Model.inheretance.model.CarRent;
import com.tss.Model.inheretance.model.TruckRent;

import com.tss.Model.inheretance.model.VehicalRental;

public class VehicalRentTest {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		BikeRent bike = new BikeRent("$32424", 100, "petrol");
		CarRent car = new CarRent("$32424", 100, "petrol");
		TruckRent truck = new TruckRent("$32424", 100, "petrol");

//		public static void display() {
//			System.out.println("rent of bike is : " + );
//			System.out.println("rent of car is : " + getRentperDay());
//			System.out.println("rent of truck is : " + getRentperDay());
//
//		}

		boolean condition = true;
		while (condition) {
			System.out.println("Enter 1 for bike rent booking ");
			System.out.println("Enter 2 for car rent booking");
			System.out.println("Enter 3 for truck rent booking");
			System.out.println("Enter 4 for display the rents for each vehical ");
			System.out.println("Enter 5 for total amount to pay ");
			System.out.println("Enter 6 to exit ");

			int number = scanner.nextInt();
			switch (number) {
			case 1: {
				bike.rentCalculate();
				break;

			}

			case 2: {
				car.rentCalculate();
				break;

			}
			case 3: {
				truck.rentCalculate();
				break;

			}
			case 4:
				bike.display();
				car.display();
				truck.display();
				break;

			case 5:
				break;

			case 6:
				condition = false;
				break;
			}

		}
	}

}
