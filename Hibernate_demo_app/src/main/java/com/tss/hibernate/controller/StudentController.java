package com.tss.hibernate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tss.hibernate.entity.Student;
import com.tss.hibernate.service.StudentService;

@RestController
@RequestMapping("/studentapp")
public class StudentController {

	@Autowired
	private StudentService studentService;

	// Add new student
	@PostMapping("/add")
	public Student addStudent(@RequestBody Student student) {
		return studentService.addNewStudent(student);
	}

	// Get all students
	@GetMapping("/all")
	public List<Student> getAllStudents() {
		return studentService.getAllStudents();

	}
	
	@GetMapping("/name")
	public Student getByname(@RequestParam String name) {
		return (Student) studentService.getByName(name);
	}


}
