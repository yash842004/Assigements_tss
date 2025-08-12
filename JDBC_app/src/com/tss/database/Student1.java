package com.tss.database;

public class Student1 {

	private int student_id;
	private String name;
	private int age;
	private double percentage;
	private int roll_no;

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public void setRoll_no(int roll_no) {
		this.roll_no = roll_no;
	}

	public Student1(int student_id, String name, int age, double percentage, int roll_no) {
		this.student_id = student_id;
		this.name = name;
		this.age = age;
		this.percentage = percentage;
		this.roll_no = roll_no;
	}

	public Student1() {
	}

	public int getStudent_id() {
		return student_id;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public double getPercentage() {
		return percentage;
	}

	public int getRoll_no() {
		return roll_no;
	}
}
