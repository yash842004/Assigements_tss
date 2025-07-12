package com.tss.model.NonViolation;

public class Pigeon implements  IFlyable {

	@Override
	public void Fly() {
		System.out.println("Pigeon can fly");
	}

	@Override
	public void getColor() {
		System.out.println("The color is blue");
	}

}
