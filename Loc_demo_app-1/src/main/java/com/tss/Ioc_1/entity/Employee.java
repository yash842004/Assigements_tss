package com.tss.Ioc_1.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class Employee {
	@Autowired
	private Dept dept;
	@Value("noob")
	private String name;
	public Dept getDept() {
		return dept;
	}


	public void setDept(Dept dept) {
		this.dept = dept;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}



	
	public Employee(Dept dept, String name) {
		super();
		this.dept = dept;
		this.name = name;
	}


	public Employee() {
		// TODO Auto-generated constructor stub
	}
	
	

	

}
