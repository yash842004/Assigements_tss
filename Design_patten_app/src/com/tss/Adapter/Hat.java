package com.tss.Adapter;

public class Hat {
	


	 public String getShortName() {
		return shortName;
	}
	public String getLongName() {
		return longName;
	}
	public double getOriginalPrice() {
		return originalPrice;
	}
	public double getDiscount() {
		return discount;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	 public Hat(String shortName, String longName, double originalPrice, double discount) {
		super();
		this.shortName = shortName;
		this.longName = longName;
		this.originalPrice = originalPrice;
		this.discount = discount;
	}
	 private String shortName;
	    private String longName;
	    private double originalPrice;
	    private double discount;
	
	

}
