package com.tss.Ioc.entity;

import org.springframework.beans.factory.annotation.Value;

public class Harddisk {
	public Harddisk(int capaticity) {
		super();
		Capaticity = capaticity;
	}

	public Harddisk() {
	}

	public int getCapaticity() {
		return Capaticity;
	}

	public void setCapaticity(int capaticity) {
		Capaticity = capaticity;
	}

	@Value("90")
	private int Capaticity;

}
