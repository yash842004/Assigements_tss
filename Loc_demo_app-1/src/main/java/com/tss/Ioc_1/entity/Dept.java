package com.tss.Ioc_1.entity;

import org.springframework.beans.factory.annotation.Value;

public class Dept {
	@Value("It dept")
	private String deptName;

	public Dept(String deptName) {
		super();
		this.deptName = deptName;
	}

	public Dept() {
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

}
