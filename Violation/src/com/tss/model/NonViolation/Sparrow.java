package com.tss.model.NonViolation;

public class Sparrow  implements IFlyable{

	@Override
	public void getColor() {

		System.out.println("brown color");
	}

	@Override
	public void Fly() {
		System.out.println("Sparrow can fly");
		
	}

}
