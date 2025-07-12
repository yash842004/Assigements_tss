package com.tss.Model;

public class StudentDetail {
	private int rollNumber;
	private String name;
	private int age;
	private int sub1;
	private int sub2;
	private int sub3;

	public int getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(int rollNumber) {
		this.rollNumber = rollNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSub1() {
		return sub1;
	}

	public void setSub1(int sub1) {
		this.sub1 = sub1;
	}

	public int getSub2() {
		return sub2;
	}

	public void setSub2(int sub2) {
		this.sub2 = sub2;
	}

	public int getSub3() {
		return sub3;
	}

	public void setSub3(int sub3) {
		this.sub3 = sub3;
	}

	public int result() {
		int FinalResult = ((sub1 + sub2 + sub3) / 3);

		return FinalResult;
	}

	public void display() {
		System.out.println("The Roll number of Student: " + rollNumber);
		System.out.println("The Name of Student: " + name);
		System.out.println("The age of Student: " + age);
		System.out.println("Marks of subject-1  : " + sub1);
		System.out.println("Marks of subject-2  : " + sub2);
		System.out.println("Marks of subject-3  : " + sub3);

	}

}
