package com.tss.factory;

public class Mahindra implements ICar {

	@Override
	public void start() {
		System.out.println("Mahindra car started");
	}

	@Override
	public void stop() {
		System.out.println("Mahindra car stoped");
	}

}
