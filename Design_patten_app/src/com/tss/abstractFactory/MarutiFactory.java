package com.tss.abstractFactory;

public class MarutiFactory implements ICarFactory {

	@Override
	public ICar makeCar() {
		System.out.println("Making the maruti car");
		return new Maruti();

	}

}
