package com.tss.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tss.model.Computer;
import com.tss.model.Hardisk;

public class ComputerTest {

	public static void main(String[] args) {

//		Hardisk hardisk = new Hardisk(20);
//		Computer computer1 = new Computer(hardisk,"ai");
//		System.out.println(computer1);

		ApplicationContext context = new ClassPathXmlApplicationContext("Config.class");
		Hardisk hardisk =(Hardisk)context.getBean("harddisk", Hardisk.class);
		System.out.println(hardisk);
		
		Computer computer =(Computer)context.getBean("computer",Computer.class);
		System.out.println(computer);

	}

}
