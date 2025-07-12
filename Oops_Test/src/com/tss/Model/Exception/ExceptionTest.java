package com.tss.Model.Exception;

public class ExceptionTest {

	public static void main(String[] args) throws ArrayIndexOutOfBoundsException, ArithmeticException {

		try {
			int number1 = 12;
			int number2 = 0;

			float result = number1 / number2;

			System.out.println("The result is " + result);

		} 
		
		catch (Exception exeption) {
			System.out.println(exeption);
			System.out.println("Do not enter the zero");

		}

		finally {
			System.out.println("It is finally block");
		}
		System.out.println("Exiting");
	}

}
