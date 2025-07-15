package com.tss.strategy.model;

public class SeniorConsultant implements IRole {

	public String getDescription() {
		return "Senior Consultant";
	}

	@Override
	public String getResponsibility() {
		return "Review code";
	}
}
