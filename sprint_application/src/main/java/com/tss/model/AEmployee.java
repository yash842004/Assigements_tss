package com.tss.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class AEmployee {
	  private int id = 2;
	  @Value("yash")
	    private String name ;
	  @Value("34930")
	    private double salary;

	    @Autowired  
	    private Department department;

	    @Override
	    public String toString() {
	        return "Employee [id=" + id + ", name=" + name + ", salary=" + salary +
	                ", department=" + department + "]";
	    }

}
