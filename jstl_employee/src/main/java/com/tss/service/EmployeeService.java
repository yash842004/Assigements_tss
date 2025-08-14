package com.tss.service;

import com.tss.dao.EmployeeDao;
import com.tss.model.Employee;

public class EmployeeService {
	private EmployeeDao employeeDao = new EmployeeDao();

	public Employee validateEmployeeLogin(String username, String password) {
		Employee emp = employeeDao.getEmployeeByUsername(username);
		if (emp != null && emp.getPassword().equals(password)) {
			return emp;
		}
		return null;
	}

	public boolean updateLeaveBalance(int empId, int newBalance) {
		employeeDao.updateLeaveBalance(empId, newBalance);
		return true;
	}

	public int getLeaveBalance(int empId) {
		System.out.println("Fetching leave balance for empId: " + empId);
		int balance = employeeDao.getLeaveBalance(empId);
		System.out.println("Retrieved balance: " + balance);
		return balance;
	}
}