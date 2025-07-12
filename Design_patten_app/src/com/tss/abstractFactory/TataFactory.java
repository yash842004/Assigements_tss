package com.tss.abstractFactory;

public class TataFactory implements ICarFactory {

	@Override
	public ICar makeCar() {
		System.out.println("Making Tata Cars");
		return new Tata();
	}

}
