package com.tss.jpa.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tss.jpa.Repositary.CourseRepositary;
import com.tss.jpa.Repositary.InstructorRepository;
import com.tss.jpa.Repositary.StudentRepositary;
import com.tss.jpa.dto.CourseReqDto;
import com.tss.jpa.dto.CourseResDto;
import com.tss.jpa.dto.StudentResponseDto;
import com.tss.jpa.entity.Course;
import com.tss.jpa.entity.Instructor;
import com.tss.jpa.entity.Student;
import com.tss.jpa.exception.StudentApiException;

@Service
public class CoureseServiceImp implements CourseService {

	@Autowired
	public StudentRepositary studentRepo;

	@Autowired
	private CourseRepositary repo;

	@Autowired
	private InstructorRepository Irepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CourseResDto addNewCourseCourseReqDto(CourseReqDto dto) {
		Course course = mapper.map(dto, Course.class);
		Course dbCourse = repo.save(course);
		return mapper.map(dbCourse, CourseResDto.class);
	}

	@Override
	public CourseResDto assignInstructor(long courseId, long instructorId) {
		Course course = repo.findById(courseId).orElseThrow(() -> new StudentApiException("Course not found"));

		Instructor instructor = Irepo.findById(instructorId)
				.orElseThrow(() -> new StudentApiException("Instructor not found"));

		course.setInstructor(instructor);

		List<Course> dbCourses = instructor.getCourse();
		dbCourses.add(course);
		instructor.setCourse(dbCourses);

		Irepo.save(instructor);
		Course updatedCourse = repo.save(course);

		return mapper.map(updatedCourse, CourseResDto.class);
	}

	@Override
	public CourseResDto updateCourseFees(long courseId, double newFees) {
		Course course = repo.findById(courseId).orElseThrow(() -> new StudentApiException("Course not found"));

		course.setFees(newFees);
		Course updatedCourse = repo.save(course);

		return mapper.map(updatedCourse, CourseResDto.class);
	}

	@Override
	public CourseResDto assignStudent(long courseId, int studentId) {

		Course dbCourse = repo.findById(courseId).orElseThrow(() -> new StudentApiException("course is not there"));

		Student dbStudent = studentRepo.findById(studentId)
				.orElseThrow(() -> new StudentApiException("Student is not there"));

		List<Course> existingCourses = dbStudent.getCourses();

		if (!existingCourses.contains(dbCourse)) {
			existingCourses.add(dbCourse);
			dbStudent.setCourses(existingCourses);

			studentRepo.save(dbStudent);
		}

		return mapper.map(dbCourse, CourseResDto.class);
	}

}
