package com.tss.Model.Test;

import com.tss.Model.Demo;

public class DemoTest {

	public static void main(String [] args) {

		Demo demo1 = new Demo();
		Demo demo2 = new Demo(); // because it has two object
//		Demo demo3 = new Demo();
		
		
//		demo1.display();
//		demo2.increment();
//		demo2.display();
//		demo1.increment();
//		demo3.display();
//		demo3.increment();	
//		demo1.display();
//		demo2.display();
//		demo3.display();
//		Demo.increment();
		
	
		
	}
	
	static {
		System.out.println("hello main static");
	}
	{
		System.out.println("Empty main"); // it will not execute.
	}

}
