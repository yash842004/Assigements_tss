package com.tss.model.violation;

public class Labour implements IWorker {

	@Override
	public void startWork() {
System.out.println("Start working");		
	}

	@Override
	public void stopWork() {
		System.out.println("Stop working");		

		
	}

	@Override
	public void eat() {
		System.out.println("Start eating");		
		
	}

	@Override
	public void drink() {
		System.out.println("Start drinking");		
		
	}

}
