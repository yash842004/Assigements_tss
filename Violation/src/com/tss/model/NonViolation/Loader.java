package com.tss.model.NonViolation;

public class Loader implements IWorker {

	@Override
	public void start() {
System.out.println("start working");		
	}

	@Override
	public void stop() {
		System.out.println("stop working");		
		
	}

	@Override
	public void eat() {
		System.out.println("start eating");		
		
	}

	@Override
	public void rest() {
		System.out.println("start resting");		
		
	}

}
