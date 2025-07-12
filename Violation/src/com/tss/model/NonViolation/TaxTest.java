package com.tss.model.NonViolation;

public class TaxTest {

	public static void main() {

		ILogger logger = new DbLogger();
		TaxCalculator calculator = new TaxCalculator(logger);
		int tax = calculator.calculateTax(1000, 2);
	}

}
