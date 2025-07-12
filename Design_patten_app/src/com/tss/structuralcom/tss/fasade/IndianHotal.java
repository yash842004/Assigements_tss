package com.tss.structuralcom.tss.fasade;

public class IndianHotal implements IHotel{

	@Override
	public IMenu getMenu() {

		return new IndianMenu();
	}

}
