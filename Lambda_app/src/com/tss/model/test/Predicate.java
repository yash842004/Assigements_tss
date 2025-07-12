package com.tss.model.test;

public class Predicate {

	public static void main(String[] args) {

		  Predicate<Integer> isAdult = (age) -> {
	            return age > 18;
	        };

	        boolean result = isAdult.test(20);
	        System.out.println("Is adult: " + result);
		
	}
}
