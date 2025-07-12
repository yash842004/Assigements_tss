package com.tss.Model.inheretance.model;

public class Method_1 implements IDemo {

	public void method3() {

		System.out.println("This method is implemented "); // default can be over ridden.

	}
	
//	public void method2() {
//		System.out.println("This method is static implementd"); // it can not be over ridden.
//	}
	
	public void method1() {
		System.out.println("This method is private implemented"); // it can be overridden
		
	}
	
	
}
