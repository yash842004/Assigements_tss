package com.tss.Ioc.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class Computer {
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public Harddisk getHarddisk() {
		return harddisk;
	}



	public void setHarddisk(Harddisk harddisk) {
		this.harddisk = harddisk;
	}


@Value("samsung")
	private String name;
@Autowired
	private Harddisk harddisk;
	
	

	public Computer(String name, Harddisk harddisk) {
		super();
		this.name = name;
		this.harddisk = harddisk;
	}



	public Computer() {
		// TODO Auto-generated constructor stub
	}

}
