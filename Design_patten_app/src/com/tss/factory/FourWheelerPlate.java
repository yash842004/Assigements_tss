package com.tss.factory;

import java.util.concurrent.atomic.AtomicInteger;

public class FourWheelerPlate implements IGenerateLicenseNumber {

	  private static final AtomicInteger fourWheelerCounter = new AtomicInteger(0);
	    private static final int MAX_COUNTER = 9999;

	@Override
	public String generatePlate() {
		int number = fourWheelerCounter.getAndIncrement();
		if (number > MAX_COUNTER) {
			number = fourWheelerCounter.getAndIncrement() % (MAX_COUNTER + 1);
		}
		return String.format("TW%04d", number);
	}

}
