package com.tss.Model;

import java.util.Scanner;

public class MainClass {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);

		StudentDetail student1 = new StudentDetail();
		System.out.println("Enter the Roll number of student:");
		student1.setRollNumber(scanner.nextInt());
		System.out.println("Enter the Name of student:");
		student1.setName(scanner.next());
		System.out.println("Enter the Age of student:");
		student1.setAge(scanner.nextInt());
		System.out.println("Enter the sub1 marks:");
		student1.setSub1(scanner.nextInt());
		System.out.println("Enter the sub2 marks:");
		student1.setSub2(scanner.nextInt());
		System.out.println("Enter the sub3 marks:");
		student1.setSub3(scanner.nextInt());
		System.out.println("Result of Student: "+student1.result());
		
		
		StudentDetail student2 = new StudentDetail();
		System.out.println("Enter the Roll number of student:");
		student1.setRollNumber(scanner.nextInt());
		System.out.println("Enter the Name of student:");
		student1.setName(scanner.next());
		System.out.println("Enter the Age of student:");
		student1.setAge(scanner.nextInt());
		System.out.println("Enter the sub1 marks:");
		student1.setSub1(scanner.nextInt());
		System.out.println("Enter the sub2 marks:");
		student1.setSub2(scanner.nextInt());
		System.out.println("Enter the sub3 marks:");
		student1.setSub3(scanner.nextInt());
		System.out.println("Result of Student: "+student2.result());
		
		
	}

}
