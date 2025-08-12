package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tss.database.Student1;

public class StudentDao {

	private Connection connection;
	private PreparedStatement preparedStatement;

	public StudentDao(Connection connection) {
		this.connection = connection;
	}

	public List<Student1> readAllStudent() {
		List<Student1> students = new ArrayList<>();



		String query = "SELECT * FROM tss_students";

		try (Statement statement = connection.createStatement(); ResultSet result = statement.executeQuery(query)) {

			while (result.next()) {
				int id = result.getInt("student_id");
				String name = result.getString("name");
				int age = result.getInt("age");
				double percentage = result.getDouble("percentage");
				int rollNo = result.getInt("roll_no");

				Student1 student = new Student1(id, name, age, percentage, rollNo);
				students.add(student);
			}

		} catch (SQLException e) {
			System.out.println("Error fetching students: " + e.getMessage());
		}

		return students;
	}

	public void addNewStudent(Student1 student) {
	
		String insertQuery = "INSERT INTO tss_students (student_id, name, age, percentage, roll_no) VALUES (?, ?, ?, ?, ?)";
		try {
			preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setInt(1, student.getStudent_id());
			preparedStatement.setString(2, student.getName());
			preparedStatement.setInt(3, student.getAge());
			preparedStatement.setDouble(4, student.getPercentage());
			preparedStatement.setInt(5, student.getRoll_no());

			int update = preparedStatement.executeUpdate();

			if (update > 0) {
				System.out.println("Student record added successfully.");
			} else {
				System.out.println("Student record was not added.");
			}

		} catch (SQLException e) {
			System.out.println("Error inserting student: " + e.getMessage());
		} finally {

		}
	}

	public void updateStudentPercentage(int studentId, double newPercentage) {
		String checkQuery = "SELECT student_id FROM tss_students WHERE student_id = ?";
		String updateQuery = "UPDATE tss_students SET percentage = ? WHERE student_id = ?";

		try {
			preparedStatement = connection.prepareStatement(checkQuery);
			preparedStatement.setInt(1, studentId);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				System.out.println("No student found with ID: " + studentId);
				return;
			}
			preparedStatement = connection.prepareStatement(updateQuery);
			preparedStatement.setDouble(1, newPercentage);
			preparedStatement.setInt(2, studentId);

			int rowsUpdated = preparedStatement.executeUpdate();

			if (rowsUpdated > 0) {
				System.out.println("Percentage updated for student ID: " + studentId);
			} else {
				System.out.println("Update failed.");
			}

		} catch (SQLException e) {
			System.out.println("Error updating student: " + e.getMessage());
		}
	}

	public void deleteStudentById(int studentId) {
		String checkQuery = "SELECT student_id FROM tss_students WHERE student_id = ?";
		String deleteQuery = "DELETE FROM tss_students WHERE student_id = ?";

		try {

			preparedStatement = connection.prepareStatement(checkQuery);
			preparedStatement.setInt(1, studentId);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				System.out.println("No student found with ID: " + studentId);
				return;
			}

			preparedStatement = connection.prepareStatement(deleteQuery);
			preparedStatement.setInt(1, studentId);
			int rowsDeleted = preparedStatement.executeUpdate();

			if (rowsDeleted > 0) {
				System.out.println("Student with ID " + studentId + " deleted successfully.");
			} else {
				System.out.println("Failed to delete student with ID: " + studentId);
			}

		} catch (SQLException e) {
			System.out.println("Error deleting student: " + e.getMessage());
		}
	}
	
	
	public Student1 getStudentById(int studentId) {
	    String query = "SELECT * FROM tss_students WHERE student_id = ?";
	    try {
	        preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setInt(1, studentId);
	        ResultSet result = preparedStatement.executeQuery();

	        if (result.next()) {
	            int id = result.getInt("student_id");
	            String name = result.getString("name");
	            int age = result.getInt("age");
	            double percentage = result.getDouble("percentage");
	            int rollNo = result.getInt("roll_no");

	            return new Student1(id, name, age, percentage, rollNo);
	        } else {
	            System.out.println("No student found with ID: " + studentId);
	        }
	    } catch (SQLException e) {
	        System.out.println("Error fetching student: " + e.getMessage());
	    }

	    return null;
	}


}
