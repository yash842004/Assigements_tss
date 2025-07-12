package com.tss.Adapter;

public class HatAdapter implements IItem {
	Hat hat = new Hat();

	@Override
	public void getName() {
		String Name = hat.getShortName() + hat.getLongName();

	}

	@Override
	public void getPrice() {

		int discount = 50;
		double price = (hat.getPrice() * discount) / 100;

	}

}
