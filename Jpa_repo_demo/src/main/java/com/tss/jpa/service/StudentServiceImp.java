package com.tss.jpa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tss.jpa.Repositary.CourseRepositary;
import com.tss.jpa.Repositary.StudentRepositary;
import com.tss.jpa.dto.StudentRequestDto;
import com.tss.jpa.dto.StudentResponseDto;
import com.tss.jpa.dto.StudentResponsePage;
import com.tss.jpa.entity.Address;
import com.tss.jpa.entity.Course;
import com.tss.jpa.entity.Student;
import com.tss.jpa.exception.StudentApiException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StudentServiceImp implements StudentService {

	@Autowired
	private StudentRepositary studentRepo;

	@Autowired
	private CourseRepositary courseRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<StudentResponseDto> getAllStudents() {
		log.info("Fetching all students");
		List<Student> dbStudent = studentRepo.findAll();
		List<StudentResponseDto> response = new ArrayList<>();

		for (Student student : dbStudent) {
			response.add(studentToStudentResponse(student));
		}
		log.info("Total students fetched: {}", response.size());
		return response;
	}

	private StudentResponseDto studentToStudentResponse(Student student) {
		StudentResponseDto dto = new StudentResponseDto();
		dto.setFirstName(student.getFirstName());
		dto.setLastName(student.getLastName());
		return dto;
	}

	@Override
	public StudentResponseDto addNewStudent(StudentRequestDto studentDto) {
		log.info("Adding new student: {} {}", studentDto.getFirstName(), studentDto.getLastName());
		Student student = mapper.map(studentDto, Student.class);
		Student dbStudent = studentRepo.save(student);
		log.info("Student saved successfully with ID: {}", dbStudent.getStudentId());
		return mapper.map(dbStudent, StudentResponseDto.class);
	}

	@Override
	public void deleteStudent(int id) {
		log.warn("Deleting student with ID: {}", id);
		studentRepo.deleteById(id);
		log.info("Student deleted successfully with ID: {}", id);
	}

	@Override
	public Student getStudentById(int id) {
		log.info("Fetching student by ID: {}", id);
		Optional<Student> student = studentRepo.findById(id);

		if (student.isEmpty()) {
			log.error("Student not found with ID: {}", id);
			throw new StudentApiException("Student with ID " + id + " not found");
		}
		log.info("Student found: {} {}", student.get().getFirstName(), student.get().getLastName());
		return student.get();
	}

	@Override
	public StudentResponsePage getStudents(int page, int size) {
		log.info("Fetching students - Page: {}, Size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		Page<Student> studentPage = studentRepo.findAll(pageable);

		List<StudentResponseDto> studentDtos = studentPage.getContent().stream()
				.map(s -> new StudentResponseDto(s.getFirstName(), s.getLastName())).toList();

		log.info("Page {} fetched with {} students", studentPage.getNumber(), studentDtos.size());
		return new StudentResponsePage(studentDtos, studentPage.getNumber(), studentPage.getTotalPages(),
				studentPage.getTotalElements());
	}

	@Override
	public Address getAddressByStudentId(Integer studentId) {
		log.info("Fetching address for student ID: {}", studentId);
		Student student = studentRepo.findById(studentId)
				.orElseThrow(() -> {
					log.error("Student not found with ID: {}", studentId);
					return new RuntimeException("Student not found with ID: " + studentId);
				});
		return student.getAddress();
	}

	@Override
	public Address updateStudentAddress(Integer studentId, Address newAddress) {
		log.info("Updating address for student ID: {}", studentId);
		Student student = studentRepo.findById(studentId)
				.orElseThrow(() -> {
					log.error("Student not found with ID: {}", studentId);
					return new RuntimeException("Student not found with ID: " + studentId);
				});

		if (student.getAddress() != null) {
			newAddress.setAddressId(student.getAddress().getAddressId());
			log.debug("Updating existing address ID: {}", student.getAddress().getAddressId());
		}

		student.setAddress(newAddress);
		studentRepo.save(student);
		log.info("Address updated successfully for student ID: {}", studentId);
		return newAddress;
	}

	@Override
	public StudentResponseDto assignCourse(int studentId, long courseId) {
		log.info("Assigning course ID: {} to student ID: {}", courseId, studentId);

		Student dbStudent = studentRepo.findById(studentId)
				.orElseThrow(() -> {
					log.error("Student not found with ID: {}", studentId);
					return new StudentApiException("Student is not there");
				});

		Course dbCourse = courseRepo.findById(courseId)
				.orElseThrow(() -> {
					log.error("Course not found with ID: {}", courseId);
					return new StudentApiException("Course does not exist");
				});

		List<Course> existingCourses = dbStudent.getCourses();

		if (existingCourses.isEmpty()) {
			existingCourses.add(dbCourse);
			dbStudent.setCourses(existingCourses);

			Student updatedStudent = studentRepo.save(dbStudent);

			List<Student> existingStudents = dbCourse.getStudents();
			existingStudents.add(updatedStudent);
			dbCourse.setStudents(existingStudents);

			courseRepo.save(dbCourse);

			log.info("Course assigned successfully. Student ID: {}, Course ID: {}", studentId, courseId);
			return mapper.map(updatedStudent, StudentResponseDto.class);
		}

		log.warn("Student ID: {} already has courses assigned. No new course added.", studentId);
		return mapper.map(dbStudent, StudentResponseDto.class);
	}

}
