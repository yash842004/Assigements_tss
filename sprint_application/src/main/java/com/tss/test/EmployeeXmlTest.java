package com.tss.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tss.model.Employees;

public class EmployeeXmlTest {
	public static void main(String[] args) {
		  ApplicationContext context = new ClassPathXmlApplicationContext("config.xml");
	        Employees emp = context.getBean("employee", Employees.class);
	        System.out.println(emp);
	}

}
