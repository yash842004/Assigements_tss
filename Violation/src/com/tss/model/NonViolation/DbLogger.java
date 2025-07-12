package com.tss.model.NonViolation;

public class DbLogger implements ILogger {

	@Override
	public void log(String err) {

		System.out.println("This is DB Logger "+err);
		
	}

}
