package com.model.exception;

public class AgeNotValideException extends Exception {

	private int age;

	public AgeNotValideException(int age) {
		super();
		this.age = age;

	}

	public String getMessage() {
		return "The enter the correct age";

	}

}
