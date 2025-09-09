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

import com.tss.jpa.Repositary.StudentRepositary;
import com.tss.jpa.dto.StudentRequestDto;
import com.tss.jpa.dto.StudentResponseDto;
import com.tss.jpa.dto.StudentResponsePage;
import com.tss.jpa.entity.Address;
import com.tss.jpa.entity.Student;
import com.tss.jpa.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/studentsjpa")
public class StudentController {

	@Autowired
	public StudentRepositary studentRepo;

	@Autowired
	public StudentService studentService;

	@PostMapping("/add")
	public ResponseEntity<StudentResponseDto> createStudent(@Valid @RequestBody StudentRequestDto student) {
		return ResponseEntity.ok().header("author", "yash").body(studentService.addNewStudent(student));
	}

	@GetMapping("/all")
	public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
		return ResponseEntity.ok(studentService.getAllStudents());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Student> getStudentById(@PathVariable int id) {
		Student student = studentService.getStudentById(id);
		return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
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

	@GetMapping("/page")
	public StudentResponsePage getStudent(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "2") int size) {
		return studentService.getStudents(page, size);
	}

	@GetMapping("/{id}/address")
	public ResponseEntity<Address> getAddress(@PathVariable Integer id) {
		return ResponseEntity.ok(studentService.getAddressByStudentId(id));
	}

	@PutMapping("/{id}/address")
	public ResponseEntity<Address> updateAddress(@PathVariable Integer id, @RequestBody Address address) {
		return ResponseEntity.ok(studentService.updateStudentAddress(id, address));
	}
	
	
	@PutMapping("/studnets/{studentId}/courses") // param courseId 
	public ResponseEntity<StudentResponseDto> assignCourse(@PathVariable int studentId, @RequestParam long courseId){
		
		
		return ResponseEntity.ok(studentService.assignCourse(studentId, courseId));
	}

}
