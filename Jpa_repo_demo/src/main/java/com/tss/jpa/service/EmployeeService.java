package com.tss.jpa.service;

import java.util.List;

import com.tss.jpa.entity.Employee;

public interface EmployeeService {

	List<Employee> getAllEmployees();

	Employee getEmployeeById(int id);

	Employee saveEmployee(Employee employee);

	void deleteEmployee(int id);

	Employee updateEmployee(Long id, Employee updatedEmployee);

}