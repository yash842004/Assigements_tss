package com.tss.fooddelivery.customer;

import java.io.Serializable;

public class Address implements Serializable  {
	private int id;
	private String name;
	private String city;
	private String state;
	private long pincode;

	public Address(int id,String name,String city, String state, long pincode) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
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
	        return id+",  "+  name+", "+ city + ", " + state + " - " + pincode;
	    }
}
