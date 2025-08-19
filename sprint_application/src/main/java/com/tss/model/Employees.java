package com.tss.model;

public class Employees {

	private int id;
	private String name;
	private double salary;
	private Department department; // injected

	public Employees() {}

	public Employees(int id, String name, double salary, Department department) {
	        this.id = id;
	        this.name = name;
	        this.salary = salary;
	        this.department = department;
	    }

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", salary=" + salary + ", department=" + department + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

}
