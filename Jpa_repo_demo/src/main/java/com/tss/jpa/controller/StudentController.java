package com.tss.jpa.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tss.jpa.entity.Student;
import com.tss.jpa.service.StudentService;

@RestController
@RequestMapping("/studentsjpa")
public class StudentController {

	@Autowired
	public StudentService studentService;

	@PostMapping("/add")
	public ResponseEntity<Student> createStudent(@RequestBody Student student) {
		return ResponseEntity.ok().header("author", "yash").body(studentService.saveStudent(student));
	}

	@GetMapping("/all")
	public ResponseEntity<List<Student>> getAllStudents() {
		return ResponseEntity.ok(studentService.getAllStudents());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable int id) {
		Student student = studentService.getStudentById(id);
		return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student studentDetails) {
		Student student = studentService.getStudentById(id);
		if (student != null) {
			student.setRollNumber(studentDetails.getRollNumber());
			student.setFirstName(studentDetails.getFirstName());
			student.setLastName(studentDetails.getLastName());
			student.setEmail(studentDetails.getEmail());
			student.setAge(studentDetails.getAge());
			return ResponseEntity.ok(studentService.saveStudent(student));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
		Student student = studentService.getStudentById(id);
		if (student != null) {
			studentService.deleteStudent(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/student")
	public List<Student> readAllStudent(@RequestParam(required = false) String name) {
		if (name == null)
			return studentService.getAllStudents();

		return studentService.readByName(name);
	}

}
