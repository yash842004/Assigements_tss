package com.tss.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AreaCalculatorTest {

	private AreaCalculator calculator;

	@BeforeEach
	void init() {
		calculator = new AreaCalculator();
	}

	@Test
	void AreaCircle() {
		assertEquals(314, calculator.AreaOfCircle(10));
	}

	@Test
	void AreaRectangle() {
		assertEquals(36, calculator.AreaOfRectangle(6, 6));

	}

}
