package com.tss.hibernate.service;

import java.util.List;

import com.tss.hibernate.entity.Employee;

public interface EmployeeService {

	Employee addEmployee(Employee employee);

	List<Employee> getAllEmployees();

	Employee getEmployeeById(Long id);

	boolean deleteEmployee(Long id);
	
}