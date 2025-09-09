package com.tss.jpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.AotInitializerNotFoundException;
import org.springframework.stereotype.Service;

import com.tss.jpa.Repositary.EmployeeRepository;
import com.tss.jpa.entity.Employee;

import jakarta.transaction.Transactional;

@Service
public class EmployeeServiceImp implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepo;

	
	@Override
	public List<Employee> getAllEmployees() {
		return employeeRepo.findAll();
	}

	// G
	@Override
	public Employee getEmployeeById(int id) {
		return employeeRepo.findById(id)
				.orElseThrow(() -> new AotInitializerNotFoundException(null, "Employee not found with id: " + id));
	}

	// Save employee (also saves SalaryAccount because of Cascade)
	@Override
	public Employee saveEmployee(Employee employee) {
		return employeeRepo.save(employee);
	}

	@Transactional
	public Employee updateEmployee(int id, Employee updatedEmployee) {
		Employee existing = getEmployeeById(id);

		existing.setName(updatedEmployee.getName());
		existing.setSalary(updatedEmployee.getSalary());

		if (updatedEmployee.getSalaryAccount() != null) {
			if (existing.getSalaryAccount() != null) {
				existing.getSalaryAccount().setAccountNo(updatedEmployee.getSalaryAccount().getAccountNo());
				existing.getSalaryAccount().setBankName(updatedEmployee.getSalaryAccount().getBankName());
				existing.getSalaryAccount().setBranch(updatedEmployee.getSalaryAccount().getBranch());
				existing.getSalaryAccount().setIfscCode(updatedEmployee.getSalaryAccount().getIfscCode());
			} else {
				existing.setSalaryAccount(updatedEmployee.getSalaryAccount());
			}
		}

		return employeeRepo.save(existing);
	}

	
	@Override
	public void deleteEmployee(int id) {
		Employee existing = getEmployeeById(id);
		employeeRepo.delete(existing);
	}

	@Override
	public Employee updateEmployee(Long id, Employee updatedEmployee) {
		// TODO Auto-generated method stub
		return null;
	}
}
