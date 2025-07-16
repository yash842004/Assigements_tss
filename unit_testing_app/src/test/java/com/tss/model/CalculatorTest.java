package com.tss.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculatorTest {

	private static Calculator calculator;

	// @BeforeEach
	@BeforeAll
	static void init() {
		calculator = new Calculator();

		System.out.println("befor each");
	}

//	@AfterEach
	@AfterAll // must use static key word.
	static void display() {
		System.out.println("after each");

	}

	@Test
	void testAddition() {

		int actualResult = calculator.addition(2, 2);

		assertEquals(4, actualResult);
		assertEquals(10, calculator.addition(5, 5));
		assertEquals(-4, calculator.addition(-2, -2));// wrong

	}

	@Test
	void testSubstraction() {

		Calculator calculator = new Calculator();
		int actualResult = calculator.substraction(2, 2);

		assertEquals(0, actualResult);
		assertEquals(4, calculator.substraction(1, -3));
	}

	@Test
	void TestMultiplication() {
		Calculator calculator = new Calculator();
		int actualResult = calculator.multiplication(6, 2);

		assertEquals(12, actualResult);
	}

	@Test
	void TestDivision() {
		Calculator calculator = new Calculator();

		int actualResult = calculator.division(6, 2);
		assertEquals(0, calculator.division(6, 0));
		assertThrows(ArithmeticException.class, () -> calculator.division(10, 0), "Number is not divisable by zero");
	}

}
