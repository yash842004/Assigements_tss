package com.tss.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Computer {
	@Autowired
	private Hardisk harkisk;
	@Value("apple")
	private String name;

	public Computer(Hardisk harkisk, String name) {
		super();
		this.harkisk = harkisk;
		this.name = name;
	}

	public Computer() {

	}

	@Override
	public String toString() {
		return "Computer [harkisk=" + harkisk + ", name=" + name + "]";
	}

	public Hardisk getHarkisk() {
		return harkisk;
	}

	public String getName() {
		return name;
	}

	public void setHarkisk(Hardisk harkisk) {
		this.harkisk = harkisk;
	}

	public void setName(String name) {
		this.name = name;
	}

}
