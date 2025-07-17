package com.tss.fooddelivery.customer;

public class Address {

	private String city;
	private String state;
	private long pincode;

	public Address(String city, String state, long pincode) {

		this.city = city;
		this.state = state;
		this.pincode = pincode;
	}
	
	 public String getCity() {
	        return city;
	    }

	    public String getState() {
	        return state;
	    }

	    public long getPincode() {
	        return pincode;
	    }

	    @Override
	    public String toString() {
	        return   city + ", " + state + " - " + pincode;
	    }
}
