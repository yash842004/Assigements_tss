package com.tss.Model.inheretance.model;

public class Circle extends Shape {
	public Circle(float radius) {
		super();
		this.radius = radius;
	}

	private float radius;

	public void area() {

		float result = (float) (3.14 * (radius * radius));
		System.out.println(result);

	}

}
