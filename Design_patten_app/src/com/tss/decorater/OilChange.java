package com.tss.decorater;

public class OilChange extends CarServiceDecorator {

	public OilChange() {
		super(carObj);
	}

	@Override
	public double getCost() {
		return 500.0 + super.getCost();
	}

}
