package com.tss.ecommers;

public class HoliInterest implements IFestivalInterest {
	private static final double HOLI_RATE = 0.068; 

	@Override
	public double getInterestRate() {
		return HOLI_RATE;
	}
}
