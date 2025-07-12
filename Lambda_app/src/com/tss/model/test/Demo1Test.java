package com.tss.model.test;

import com.tss.model.Demo1;
import com.tss.model.IDemo1;

public class Demo1Test {

	public static void main(String[] args) {

		IDemo1 demo = Demo1::display;
		show(demo);
		Demo1 d1 = new Demo1();

		IDemo1 demo1 = d1::show;
		show(demo1);

	}

	private static void show(IDemo1 demo) {
		demo.accept();
	}
}