package com.tss.structural.composite.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Manager implements IEmployee{
	 public String getName() {
		return name;
	}

	public long getEmpId() {
		return empId;
	}

	public List<IEmployee> getSubordinates() {
		return subordinates;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public void setSubordinates(List<IEmployee> subordinates) {
		this.subordinates = subordinates;
	}

	 private String name;
	    private long empId;
	    private List<IEmployee> subordinates = new ArrayList();

    public Manager(String name, long empId) {
        this.name = name;
        this.empId = empId;
    }
    
  
    
    
    public void add(IEmployee emp) {
    	subordinates.add(emp);
    	
    }
    public void remove(IEmployee emp) {
    	subordinates.remove(emp);
    }

	public void employeeDetails() {

		System.out.println(empId+" "+name);
		
		for(IEmployee emp : subordinates) {
			emp.employeeDetails();
		}
		
	}

}
