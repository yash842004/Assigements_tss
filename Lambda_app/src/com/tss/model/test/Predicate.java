package com.tss.model.test;

public class Predicate {

	public static void main(String[] args) {

		  Predicate isAdult = age() -> {
	            return this.age > 18;
	        };

	        boolean result = ((Object) isAdult).test(20);
	        System.out.println("Is adult: " + result);
		
	}

	private static Predicate age() {
		// TODO Auto-generated method stub
		return null;
	}
}
