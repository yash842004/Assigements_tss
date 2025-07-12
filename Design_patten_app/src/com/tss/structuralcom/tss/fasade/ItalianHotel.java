package com.tss.structuralcom.tss.fasade;

public class ItalianHotel implements IHotel{

	@Override
	public IMenu getMenu() {
		return new ItalianMenu();
	}



}
