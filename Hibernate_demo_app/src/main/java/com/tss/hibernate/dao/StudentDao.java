package com.tss.hibernate.dao;

import java.util.List;

import com.tss.hibernate.entity.Student;

public interface StudentDao {
	
    Student save(Student student);
    List<Student> findAll();
    
    Student readStudentById(int studentId);
    
    List<Student> getByName();
}
