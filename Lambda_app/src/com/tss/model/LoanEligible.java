package com.tss.model;

public class LoanEligible {
	private String name;
	private int age;
	private float income;
	private int creditScore;

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public float getIncome() {
		return income;
	}

	public int getCreditScore() {
		return creditScore;
	}

	public LoanEligible(String name, int age, float income, int creditScore) {
		super();
		this.name = name;
		this.age = age;
		this.income = income;
		this.creditScore = creditScore;
	}

}
