package com.tss.Model.ExceptionHandling;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.model.exception.InsufficientWaterException;
import com.model.exception.OverfillException;

public class WaterTank {

	private int waterFlow;
	private final int maximiumCapacityIs = 100;
	private int TotalWaterIsThere;

	public WaterTank(int waterFlow) {
		super();
		this.waterFlow = waterFlow;
	}

	public WaterTank() {

	}

	public void FillWater(Scanner scanner) throws OverfillException {
		System.out.println("Current water level: " + this.waterFlow + " Liter.");
		System.out.println("Maximum tank capacity: " + this.maximiumCapacityIs + " Liter.");
		System.out.print("Enter the amount of water to fill: ");

		int amountToFill;
		try {
			amountToFill = scanner.nextInt();
		} catch (InputMismatchException exception) {
			System.out.println("Invalid input! Please enter a valid number for the amount to fill.");
			scanner.next();
			return;
		}

		if (amountToFill <= 0) {
			System.out.println("Amount to fill must be greater than 0.");
			return;
		}

		if (this.waterFlow + amountToFill <= this.maximiumCapacityIs) {
			this.waterFlow += amountToFill;
			this.TotalWaterIsThere = this.waterFlow;

			System.out.println("Successfully filled: " + amountToFill + " Liter.");
			System.out.println("Total water in tank: " + this.TotalWaterIsThere + " Liter.");
		}
		throw new OverfillException(waterFlow);

	}

	public void DispenseWater(Scanner scanner) throws InsufficientWaterException {
		System.out.println("Current water level: " + this.waterFlow + " Liter.");
		System.out.print("Enter the amount of water to dispense: ");

		int amountToDispense;
		try {
			amountToDispense = scanner.nextInt();
		} catch (InputMismatchException exception) {
			System.out.println("Invalid input! Please enter a valid number for the amount to fill.");
			scanner.next();
			return;
		}

		if (amountToDispense <= 0) {
			System.out.println("Amount to dispense must be greater than 0.");
			return;
		}

		if (this.waterFlow >= amountToDispense) {
			this.waterFlow -= amountToDispense;
			this.TotalWaterIsThere = this.waterFlow;

			System.out.println("Successfully dispensed: " + amountToDispense + " Liter.");
			System.out.println("Amount of water remaining: " + this.waterFlow + " Liter.");
		}
		throw new InsufficientWaterException(waterFlow);

	}

	public void Guide() {
		System.out.println();
		System.out.println("Enter 1 for Dispense the water from the tank");
		System.out.println("Enter 2 for fill the water in the tank");
		System.out.println("Enter 3 for Exit");

	}

	public void Display() {
		System.out.println("Tank Status: " + this.waterFlow + " out of  " + this.maximiumCapacityIs + " Liter.");
	}

}
