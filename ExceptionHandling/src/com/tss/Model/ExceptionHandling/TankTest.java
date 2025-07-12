package com.tss.Model.ExceptionHandling;

import java.util.Scanner;

import com.model.exception.InsufficientWaterException;
import com.model.exception.OverfillException;

public class TankTest {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		WaterTank tank1 = new WaterTank();

		boolean condition = true;
		while (condition) {

			tank1.Guide();
			try {
				int number = scanner.nextInt();
				switch (number) {

				case 1:
					tank1.DispenseWater(scanner);
					break;
				case 2:
					tank1.FillWater(scanner);
					break;
				case 3:
					condition = false;
					break;

				}
			} catch (InsufficientWaterException exception1) {
				System.out.println(exception1.getMessage());
			} catch (OverfillException exception2) {
				System.out.println(exception2.getMessage());
			} finally {
			}

			tank1.Display();

		}

	}
}
