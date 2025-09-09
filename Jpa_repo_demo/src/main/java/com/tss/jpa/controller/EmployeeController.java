package com.tss.jpa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.jpa.entity.Employee;
import com.tss.jpa.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employeesjpa")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeService.saveEmployee(employee);
	}

	@GetMapping("/all")
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
		Employee employee = employeeService.getEmployeeById(id);
		return employee != null ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
		return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
		Employee employee = employeeService.getEmployeeById(id);
		if (employee != null) {
			employeeService.deleteEmployee(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/{id}/salaryAccount")
	public ResponseEntity<?> getSalaryAccountByEmployeeId(@PathVariable int id) {
		Employee employee = employeeService.getEmployeeById(id);
		if (employee != null && employee.getSalaryAccount() != null) {
			return ResponseEntity.ok(employee.getSalaryAccount());
		}
		return ResponseEntity.notFound().build();
	}

}
