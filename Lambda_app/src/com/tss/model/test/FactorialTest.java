package com.tss.model.test;

import com.tss.model.IFactorial;

public class FactorialTest {

	public static void main(String[] args) {

		IFactorial factorial = (number) -> {
			int result = 1;
			for (int i = 1; i <= number; i++) {
				result *= i;
			}
			return result;
		};

		int result = factorial.factorial(5);
		System.out.println(result);
	}
}
