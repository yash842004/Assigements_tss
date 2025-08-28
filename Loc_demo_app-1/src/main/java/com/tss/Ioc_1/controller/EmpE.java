package com.tss.Ioc_1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.Ioc_1.entity.Employee;

@RestController
@RequestMapping("/app")
public class EmpE {
	@Autowired()
	private Employee employee;

	@GetMapping("/employee")
	public Employee getEmployee() {

		return employee;
	}

	@GetMapping("/employee/name")
	public String getName() {
		return employee.getName();
	}

}
