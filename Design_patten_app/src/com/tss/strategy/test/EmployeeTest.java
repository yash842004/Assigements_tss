package com.tss.strategy.test;

import com.tss.strategy.model.Consultant;
import com.tss.strategy.model.Employee;
import com.tss.strategy.model.SeniorConsultant;
import com.tss.strategy.model.TechLead;

public class EmployeeTest {

	public static void main(String[] args) {

		Employee employee = new Employee(1, "John", new Consultant());
		employee.displayInfo();

		System.out.println("\nAfter promotion to Senior Consultant:\n");
		employee.promote(new SeniorConsultant());
		employee.displayInfo();

		System.out.println("\nAfter promotion to Tech Lead:\n");
		employee.promote(new TechLead());
		employee.displayInfo();
	}

}
