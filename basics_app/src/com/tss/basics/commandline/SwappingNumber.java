package com.tss.basics.commandline;

public class SwappingNumber {
	public static void main(String[] args) {
		
		int number01 =  Integer.parseInt(args[0]);
		System.out.println("Value of number01 befor swapping = "+number01);
		int number02 = Integer.parseInt(args[1]);
		System.out.println("Value of number02 befor swapping "+number02);
					
		int temp = number01;
		number01 = number02;
		number02 = temp;
		System.out.println("Value of number01 after swapping = "+number01);
		System.out.println("Value of number02 after swapping "+number02);
		
		int number1 = Integer.parseInt(args[2]);
		int number2 = Integer.parseInt(args[3]);
		System.out.println("befor swapping the number1 " +number1);
		System.out.println("befor swapping the number2 " +number2);
		
		number1 = number1 + number2;
		number2 = number1 - number2;
		number1 = number1 - number2;
		System.out.println("after swapping the number1 " +number1);
		System.out.println("after swapping the number1 " +number2);
		
	}

}