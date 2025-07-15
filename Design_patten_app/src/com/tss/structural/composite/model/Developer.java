package com.tss.structural.composite.model;

public class Developer implements IEmployee {

	private String name;
	private int empId;

	public Developer(String name, int empId) {

		this.name = name;
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public int getEmpId() {
		return empId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public void employeeDetails() {
		System.out.println(empId + " " + name);

	}

}
