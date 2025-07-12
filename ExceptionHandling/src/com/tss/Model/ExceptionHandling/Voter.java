package com.tss.Model.ExceptionHandling;

import com.model.exception.AgeNotValideException;

public class Voter {
	
	private int voterId;
	private String Name;
	private int age;
	
	public int getVoterId() {
		return voterId;
	}
	public void setVoterId(int voterId) {
		this.voterId = voterId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getAge() {
		
		return age;
	}
	public void setAge(int age) throws AgeNotValideException {
		if(age < 18)
			throw new AgeNotValideException(age);
		this.age = age;
	}
	@Override
	public String toString() {
		return "Voter [voterId=" + voterId + ", Name=" + Name + ", age=" + age + "]";
	}

	public Voter(int voterId, String name, int age) throws AgeNotValideException {
		super();
		this.voterId = voterId;
		Name = name;
		if(age < 18)
			throw new AgeNotValideException(age);
		this.age = age;
	}


}
