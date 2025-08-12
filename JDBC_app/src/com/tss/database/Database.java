package com.tss.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection = null;

    public  Database() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tss_students", "root", "root");
            System.out.println("Connection established successfully");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readAllStudents() {
        String query = "SELECT * FROM tss_students";

        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(query)) {

            while (result.next()) {
                System.out.println(
                        result.getInt("student_id") + "\t" + result.getString("name") + "\t" + result.getInt("age")
                                + "\t" + result.getDouble("percentage") + "\t" + result.getInt("roll_no"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertData(Student student) {
        try (Statement statement = connection.createStatement()) {

            String sql = "INSERT INTO tss_students VALUES (" +
                    student.getStudent_id() + ", '" +
                    student.getName() + "', " +
                    student.getAge() + ", " +
                    student.getPercentage() + ", " +
                    student.getRoll_no() + ")";

            int result = statement.executeUpdate(sql);

            if (result > 0) {
                System.out.println("Student is added successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
