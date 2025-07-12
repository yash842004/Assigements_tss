package com.tss.Model;

public class Demo {
	
	
	public Demo() {
		System.out.println("demo constrater");
	}

	private int a;
	private int b;
	private static int c;

	public static void increment() {
//		a++;
//		b++;
		c++;
	}

	public void display() {
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);

	}
	
	{
		System.out.println("empty block");
	}
	static {
		System.out.println("static in outside class");
	}
}
