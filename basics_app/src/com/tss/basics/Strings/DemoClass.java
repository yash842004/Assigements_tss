
package com.tss.basics.Strings;

public class DemoClass {
	public static void main(String[] args) {

		String name = new String("hello");
		System.out.println(name);
		char demo = 'h';

		StringBuffer mutableString = new StringBuffer("om");
		System.out.println(mutableString);
		mutableString.append(" shamti");
		System.out.println(mutableString);
		System.out.println(mutableString.charAt(3));
		mutableString.setCharAt(0, demo);
		System.out.println(mutableString);

		System.out.println(mutableString.indexOf("m"));

	}

}