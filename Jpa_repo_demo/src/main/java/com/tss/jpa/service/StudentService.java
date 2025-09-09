package com.tss.jpa.service;

import java.util.List;

import com.tss.jpa.dto.StudentRequestDto;
import com.tss.jpa.dto.StudentResponseDto;
import com.tss.jpa.dto.StudentResponsePage;
import com.tss.jpa.entity.Address;
import com.tss.jpa.entity.Student;

public interface StudentService {

	List<StudentResponseDto> getAllStudents();

	Student getStudentById(int id);

	StudentResponseDto addNewStudent(StudentRequestDto studentRequestDto);

	void deleteStudent(int id);

	StudentResponsePage getStudents(int page, int size);

	Address getAddressByStudentId(Integer studentId);

	Address updateStudentAddress(Integer studentId, Address newAddress);
	
	StudentResponseDto assignCourse(int studentId, long courseId);

}
