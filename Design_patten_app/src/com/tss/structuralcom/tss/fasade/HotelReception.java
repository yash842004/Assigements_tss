package com.tss.structuralcom.tss.fasade;

public class HotelReception {
	public void getIndianMenu() {
		
		IHotel indHotel = new IndianHotal();
		IMenu menu = indHotel.getMenu();
		menu.dispalyMenu();
		
	}

	public void getItalianMenu() {
		IHotel itaHotel = new ItalianHotel();
		IMenu menu = itaHotel.getMenu();
		menu.dispalyMenu();
		
	}
}
