package com.tss.factory;

public class Maruti implements ICar{

	@Override
	public void start() {

		System.out.println("maruti car start");
	}

	@Override
	public void stop() {
		System.out.println("maruti car stop");
		
	}

}
