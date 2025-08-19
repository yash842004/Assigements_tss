package com.tss.model;

import org.springframework.stereotype.Component;

@Component
public class ADepartment {
	private int id = 201;
	private String name = "Finance";

	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}

}
