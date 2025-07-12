package com.tss.Model.inheretance.model;

public class NewCircle implements IShape {

	@Override
	public void area() {
		int radius = 12;
		float mathPi = (float) 3.14;

		float result = mathPi * (radius * radius);
		System.out.println("the result of circle " + result);

	}

}
