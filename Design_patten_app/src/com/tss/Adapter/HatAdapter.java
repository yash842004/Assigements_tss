package com.tss.Adapter;

public class HatAdapter implements IItem {
	Hat hat = new Hat(null, null, 0, 0);

	public void getName() {
		String Name = hat.getShortName() + hat.getLongName();

	}

	@Override
	public double getPrice() {

		int discount = 50;
		double price = (hat.getOriginalPrice() * discount) / 100;
		return price;

	}

	@Override
	public String getItemName() {
		// TODO Auto-generated method stub
		return null;
	}

}
