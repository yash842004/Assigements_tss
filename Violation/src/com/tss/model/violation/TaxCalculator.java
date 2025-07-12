package com.tss.model.violation;

public class TaxCalculator extends DBLogger {

	private DBLogger dbLogger;

	void TaxCalculator() {

		dbLogger = new DBLogger();

	}

	public int calculateTax(int amount, int rate) {
		int tax = 0;
		try {

			tax = amount / rate;
			System.out.println(tax);
		} catch (Exception e) {
			new DBLogger().log("Divide by zero");

		}
		return tax;
	}

}
