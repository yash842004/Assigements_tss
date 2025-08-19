package com.tss.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.tss.model.AEmployee;
import com.tss.model.Config;

public class TestAnnotation {
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		AEmployee emp = context.getBean(AEmployee.class); 
		System.out.println(emp);
	}

}
