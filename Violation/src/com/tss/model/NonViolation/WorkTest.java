package com.tss.model.NonViolation;

public class WorkTest {

	public static void main(String args[]) {

		Loader loader = new Loader();
		System.out.println("It is labour method");
		loader.start();
		loader.stop();
		loader.eat();
		loader.rest();

		Robot robot = new Robot();
		System.out.println("It is robot method");

		robot.start();
		robot.stop();

	}

}
