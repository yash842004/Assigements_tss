package com.tss.dao;

import com.tss.model.Employee;
import java.util.ArrayList;
import java.util.List;

public class employeeDao {

    // In real app: fetch from DB. Here: simulated data
    public static List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1, "Om", 45000));
        employees.add(new Employee(2, "Raj", 55000));
        employees.add(new Employee(3, "Rohan", 60000));
        return employees;
    }
}
