package com.tss.Model;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Employee {

	public Employee() {
		empId = 00;
		name = "na";
		joiningDate = "0000-00-00";
		salary = 00;
	}

	public Employee(int empId, String name, String joiningDate, int salary) {

	}

	private int empId;
	private String name;
	private String joiningDate;
	private long salary;

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setJoininDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getJoiningDate() {
		return joiningDate;

	}

	public void setSalary(long salary) {
		this.salary = salary;
	}

	public long getSalary() {
		return salary;

	}

	public int experiences() {
		// try {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate joinDate = LocalDate.parse(getJoiningDate(), formatter);
		LocalDate currentDate = LocalDate.now();
		return currentDate.getYear() - joinDate.getYear();

//		catch (DateTimeParseException e) {
//			System.out.println("Please use yyyy-MM-dd format.");
//			return 0;
//		}
	}

	public long bonus() {

		long salaryBonus = (salary / 2);

		return salaryBonus;
	}

	public void display() throws ParseException {

		System.out.println("The Id of employee: " + getEmpId());
		System.out.println("The name of employee: " + getName());
		System.out.println("The Date of Joining is: " + getJoiningDate());
		System.out.println("The Salary of employee is: " + getSalary());
		System.out.println("The Bonues of employee is: " + bonus());
		System.out.println(" The Experience of the employee is: " + experiences() + " year");

	}

}
