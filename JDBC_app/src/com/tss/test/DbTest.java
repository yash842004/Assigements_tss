package com.tss.test;

import java.sql.SQLException;
import java.util.Scanner;

import com.tss.database.Database;
import com.tss.database.Student;

public class DbTest {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		try {

			System.out.print("Enter student ID: ");
			int student_id = sc.nextInt();

			System.out.print("Enter name: ");
			String name = sc.next();

			System.out.print("Enter age: ");
			int age = sc.nextInt();

			System.out.print("Enter percentage: ");
			double percentage = sc.nextDouble();

			System.out.print("Enter roll number: ");
			int roll_no = sc.nextInt();

			Student student = new Student(student_id, name, age, percentage, roll_no);

			Database studentDatabase = new Database();
			studentDatabase.insertData(student);
			studentDatabase.readAllStudents();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
	}
}
