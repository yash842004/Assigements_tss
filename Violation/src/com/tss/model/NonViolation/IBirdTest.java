package com.tss.model.NonViolation;

public class IBirdTest {

	public static void main(String[] args) {

		Pigeon pigeon = new Pigeon();
		System.out.println("Detail about pigeon");
		pigeon.getColor();
		pigeon.Fly();
		Sparrow sparrow = new Sparrow();
		System.out.println("Detail about Sparrow");
		sparrow.getColor();
		sparrow.Fly();
		Ostriches ostriches = new Ostriches();
		System.out.println("Detail about Ostriches");
		ostriches.getColor();
		ostriches.walk();
		;

	}

}
