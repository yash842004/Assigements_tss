package com.tss.Model.Test;

public class StringTest {

	public static void main(String[] args) {

		StringBuilder name1 = new StringBuilder("abc");
		StringBuilder name2 = new StringBuilder("xyz");

		System.out.println(name1.hashCode());
		System.out.println(name2.hashCode());

		System.out.println(name1 == name2);

		name1 = name2;

		System.out.println(name1.hashCode());
		System.out.println(name1.hashCode());

		System.out.println(name2 == name1);

	}

}
