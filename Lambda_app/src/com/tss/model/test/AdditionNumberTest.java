package com.tss.model.test;

import java.util.function.BiConsumer;

import com.tss.model.IAdditionNumber;

public class AdditionNumberTest {

	public static void main(String[] args) {

		BiConsumer<Integer,Integer> number = (number1, number2) -> {
			int result = number1 + number2;
			System.out.println("Addition of numbers -> "+result);
		};
		number.accept(10, 10);
		

		
	}
}
