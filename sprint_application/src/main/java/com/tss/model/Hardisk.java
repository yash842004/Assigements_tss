package com.tss.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Hardisk {

	public Hardisk(int harkDisk) {
		super();
		this.capacity = harkDisk;
	}

	@Override
	public String toString() {
		return "Hardisk [harkDisk=" + capacity + "]";
	}
	@Value("500")
	private int capacity;

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}
