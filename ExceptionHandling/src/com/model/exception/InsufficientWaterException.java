package com.model.exception;

public class InsufficientWaterException extends RuntimeException {

	private int waterFlow;

	public InsufficientWaterException(int waterFlow) {
		super();
		this.waterFlow = waterFlow;
	}

	public String getMessage() {

		return "The water tank do not have sufficient water in the tank";

	}

}
