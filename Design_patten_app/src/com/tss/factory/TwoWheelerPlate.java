package com.tss.factory;

import java.util.concurrent.atomic.AtomicInteger;

public class TwoWheelerPlate implements IGenerateLicenseNumber {

	private static final AtomicInteger twoWheelerCounter = new AtomicInteger(0);
	private static final int MAX_COUNTER = 9999;

	public String generatePlate() {
		int number = twoWheelerCounter.getAndIncrement();
		if (number > MAX_COUNTER) {
			number = twoWheelerCounter.getAndIncrement() % (MAX_COUNTER + 1);
		}
		return String.format("TW%04d", number);
	}

}
