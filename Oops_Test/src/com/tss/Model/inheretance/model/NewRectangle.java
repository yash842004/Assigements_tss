package com.tss.Model.inheretance.model;

public class NewRectangle implements IShape {

	@Override
	public void area() {
		int length = 10;
		int breadh = 12;
		int result = length * breadh;
		System.out.println("the result of rectangle " + result);
	}

}
