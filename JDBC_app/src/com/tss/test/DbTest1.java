package com.tss.test;

import com.tss.controller.StudentController;
import com.tss.controller.MetaData;
import java.util.Scanner;

public class DbTest1 {

    public static void main(String[] args) {
        StudentController controller = new StudentController();
        MetaData metaData = new MetaData();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("1. Read All Students");
            System.out.println("2. Add New Student");
            System.out.println("3. Get Student by ID");
            System.out.println("4. Update Student Percentage");
            System.out.println("5. Delete Student by ID");
            System.out.println("6. Database Metadata");
            System.out.println("7. Table Metadata");
            System.out.println("8. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    controller.readAllRecords();
                    break;
                case 2:
                    controller.addNewStudent();
                    break;
                case 3:
                    controller.getStudentById();
                    break;
                case 4:
                    controller.updateStudentPercentage();
                    break;
                case 5:
                    controller.deletStudentById();
                    break;
                case 6:
                    metaData.databaseMetaData();
                    break;
                case 7:
                    System.out.print("Enter table name: ");
                    String tableName = scanner.nextLine();
                    metaData.tableMetaData(tableName);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
