package com.tss.prototype;

public class Car extends Vehical {

	private String carType;

	public Car(String model, String color, Engine engine, String carType) {
		super(model, color, engine);
		this.carType = carType;

	}

	@Override
	public void drive() {
		System.out.println("Driving the " + getColor() + " " + carType + " " + getModel() + " car.");
	}

}
