package com.tss.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class StudentTest {

	private StudentService studentService;
	private Student student;

	@BeforeEach
	void init() {
		studentService = Mockito.mock(StudentService.class);
		student = new Student(studentService);
	}

	@Test
	void testCalculateAverage() {

		when(studentService.getFinalMarks()).thenReturn(450);
		when(studentService.getNumberOfSubjects()).thenReturn(9);

		assertEquals(50, student.calculateAverage());
	}

}