package com.tss.ecommers;

public class DiwaliInterest implements IFestivalInterest {

	private static final double DIWALI_RATE = 0.075; 

	@Override
	public double getInterestRate() {
		return DIWALI_RATE;
	}
}