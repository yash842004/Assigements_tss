package com.tss.Model.inheretance.model;

public class Rectangle extends Shape {
	public Rectangle(int length, int breath) {
		super();
		this.length = length;
		this.breath = breath;
	}

	private int length;
	private int breath;

	public void area() {
		int result = length * breath;
		System.out.println(result);
	}

}
