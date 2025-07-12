package com.model.exception;

public class OverfillException extends RuntimeException {

	private int waterFlow;

	public OverfillException(int waterFlow) {
		super();
		this.waterFlow = waterFlow;
	}

	public String getMessage() {
		return "The watertank is over flow";

	}

}
