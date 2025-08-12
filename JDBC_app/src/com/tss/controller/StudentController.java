package com.tss.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.tss.dao.StudentDao;
import com.tss.dao.services.StudentServices;
import com.tss.database.Student1;
import com.tss.database.Database1;

public class StudentController {

	private Scanner scanner = new Scanner(System.in);
	private StudentServices studentService;

	public StudentController() {
		Connection connection = Database1.connect(); 
		StudentDao studentDao = new StudentDao(connection);
		this.studentService = new StudentServices(studentDao);
	}

	public void readAllRecords() {
		List<Student1> students = studentService.readAllRecords();

		if (students == null || students.isEmpty()) {
			System.out.println("No student records found.");
			return;
		}

		for (Student1 student : students) {
			System.out.println(student.getStudent_id() + "\t" + student.getName() + "\t" + student.getAge() + "\t"
					+ student.getPercentage() + "\t" + student.getRoll_no());
		}
	}

	public void addNewStudent() {
		System.out.println("Enter the student ID:");
		int studentId = scanner.nextInt();
		scanner.nextLine();

		System.out.println("Enter the name of the student:");
		String name = scanner.nextLine();

		System.out.println("Enter the age:");
		int age = scanner.nextInt();

		System.out.println("Enter the percentage:");
		float percentage = scanner.nextFloat();

		System.out.println("Enter the roll number:");
		int rollNo = scanner.nextInt();

		Student1 student = new Student1(studentId, name, age, percentage, rollNo);
		studentService.addNewStudent(student);
	}

	public void updateStudentPercentage() {
		System.out.print("Enter the student ID: ");
		int studentId = scanner.nextInt();

		System.out.print("Enter the new percentage: ");
		double newPercentage = scanner.nextDouble();

		studentService.updateStudentPercentage(studentId, newPercentage);
	}

	public void deletStudentById() {
		System.out.print("Enter the student ID to delete: ");
		int studentId = scanner.nextInt();
		Student1 student = new Student1();
		student.setStudent_id(studentId);

		studentService.deleteStudentById(student);
	}
	
	public void getStudentById() {
	    System.out.print("Enter student ID to fetch: ");
	    int studentId = scanner.nextInt();

	    Student1 student = studentService.getStudentById(studentId);

	    if (student != null) {
	        System.out.println("Student Details:");
	        System.out.println("ID: " + student.getStudent_id());
	        System.out.println("Name: " + student.getName());
	        System.out.println("Age: " + student.getAge());
	        System.out.println("Percentage: " + student.getPercentage());
	        System.out.println("Roll No: " + student.getRoll_no());
	    } else {
	        System.out.println("Student not found.");
	    }
	}

}
