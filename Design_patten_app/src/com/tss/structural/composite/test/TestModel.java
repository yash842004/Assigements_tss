package com.tss.structural.composite.test;

import com.tss.structural.composite.model.Developer;
import com.tss.structural.composite.model.Manager;

public class TestModel {

	public static void main(String[] args) {


        Manager generalManager = new Manager("Rajesh", 1001);
        Developer dev1 = new Developer("Ankit", 2001);
        Developer dev2 = new Developer("Priya", 2002);

        generalManager.add(dev1);
        generalManager.add(dev2);

        generalManager.employeeDetails();
	}

}
