package com.tss.model.NonViolation;

public class Ostriches implements  INonFlyable {

	@Override
	public void walk() {

		System.out.println("Ostriches can walk and run");
	}

	@Override
	public void getColor() {

		System.out.println("Ostriches is an black color");
	}

}
