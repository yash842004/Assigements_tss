package com.tss.model.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tss.model.Employee;

public class EmployeeTest {

    public static void main(String[] args) {

        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee(1, "raj", 431654.00, "manager"));
        employees.add(new Employee(2, "rohan", 123654.00, "manager"));
        employees.add(new Employee(3, "ronak", 1334564.00, "AI"));
        employees.add(new Employee(4, "ved", 133456.00, "AI"));
        employees.add(new Employee(5, "ash", 13546.30, "IT"));
        employees.add(new Employee(6, "mona", 13645.30, "AI"));

        System.out.println("Sorted Employees (salary high to low):");
        employees.sort(Comparator.comparing(Employee::getSalary).reversed());
        for (Employee emp : employees) {
            System.out.println(emp.getId() + " - " + emp.getName() + " - " + emp.getSalary() + " - " + emp.getDepartment());
        }
        System.out.println("\nHighest Paid Employee by Department:");
        Map<String, Optional<Employee>> highestPaid = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.maxBy(Comparator.comparing(Employee::getSalary))));
        
        
        for (String dept : highestPaid.keySet()) {
            Optional<Employee> empOpt = highestPaid.get(dept);
            if (empOpt.isPresent()) {
                Employee emp = empOpt.get();
                System.out.println(dept + " -> " + emp.getName() + " | Salary: " + emp.getSalary());
            }
        }
    }
}
