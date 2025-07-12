package com.tss.furniture.Test;

import java.util.Scanner;

import com.tss.furniture.IFurniture;
import com.tss.furniture.IFurnitureFactory;
import com.tss.furniture.ModernFurnitureFactory;
import com.tss.furniture.VictorianFurnitureFactory;

public class FurnitureTest {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		IFurnitureFactory selectedFactory = null;

		while (selectedFactory == null) {
			System.out.println("Please select a furniture style:");
			System.out.println("1. Modern");
			System.out.println("2. Victorian");
			System.out.print("Enter your choice 1 or 2: ");

			try {
				int styleChoice = scanner.nextInt();
				scanner.nextLine();
				switch (styleChoice) {
				case 1:
					selectedFactory = new ModernFurnitureFactory();
					System.out.println("You selected Modern style. Let's start picking items!");
					break;
				case 2:
					selectedFactory = new VictorianFurnitureFactory();
					System.out.println("You selected Victorian style. Let's start picking items!");
					break;
				default:
					System.out.println("Invalid style choice. Please enter 1 or 2.");
				}
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter a number (1 or 2).");
				scanner.nextLine();
			}
		}

		System.out.println("1. Table");
		System.out.println("2. Chair");
		System.out.println("3. Sofa");
		System.out.print("Enter your choice (1-3): ");

		try {
			int itemChoice = scanner.nextInt();
			scanner.nextLine();

			IFurniture furnitureItem = null;

			switch (itemChoice) {
			case 1:
				furnitureItem = selectedFactory.createTable();
				break;
			case 2:
				furnitureItem = selectedFactory.createChair();
				break;
			case 3:
				furnitureItem = selectedFactory.createSofa();
				break;

			}

			if (furnitureItem != null && itemChoice != 4) {
				furnitureItem.describe();
			}

		} catch (Exception e) {
			System.out.println("Invalid input. Please enter a number.");
			scanner.nextLine();
		}

	}
}