package com.tss.dao.services;

import java.util.List;
import com.tss.dao.StudentDao;
import com.tss.database.Student1;

public class StudentServices {

    private StudentDao studentDao;

    public StudentServices(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public List<Student1> readAllRecords() {
        return studentDao.readAllStudent();
    }
    
    public void addNewStudent(Student1 student) {
    	if(student.getAge() < 18) {
    		System.out.println("age must be greater then 18");
    		return ;
    	}	
    	
    	studentDao.addNewStudent(student);
    }
    
    public void updateStudentPercentage(int studentId, double percentage) {
        if (percentage < 0 || percentage > 100) {
            System.out.println("Percentage must be between 0 and 100.");
            return;
        }
        studentDao.updateStudentPercentage(studentId, percentage);
    }
    
    public void deleteStudentById(Student1 student) {
        if (student == null || student.getStudent_id() <= 0) {
            System.out.println("Invalid student details provided.");
            return;
        }

        studentDao.deleteStudentById(student.getStudent_id());
    }
    
    public Student1 getStudentById(int studentId) {
        return studentDao.getStudentById(studentId);
    }




}
