package com.tss.factory;

import java.util.concurrent.atomic.AtomicInteger;

public class HeavyVehicalPlate implements IGenerateLicenseNumber {
	private static final AtomicInteger heavyVehicleCounter = new AtomicInteger(0);
	private static final int MAX_COUNTER = 999999;

	@Override
	public String generatePlate() {
		int number = heavyVehicleCounter.getAndIncrement();
		if (number > MAX_COUNTER) {
			number = heavyVehicleCounter.getAndIncrement() % (MAX_COUNTER + 1);
		}
		return String.format("HV%06d", number);
	}

}
