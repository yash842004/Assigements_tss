package com.tss.Model.inheretance.model;

public interface IDemo {
	private void method1() {
		System.out.println("It is private");

	}

	static void method2() {
		System.out.println("It is static");

	}

	default void method3() {
		System.out.println("It is default");
		
	}

}
