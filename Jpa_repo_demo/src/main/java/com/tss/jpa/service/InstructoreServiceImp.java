package com.tss.jpa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tss.jpa.Repositary.CourseRepositary;
import com.tss.jpa.Repositary.InstructorRepository;
import com.tss.jpa.dto.CourseResDto;
import com.tss.jpa.dto.InstructorRepDto;
import com.tss.jpa.dto.InstructoresReqDto;
import com.tss.jpa.entity.Course;
import com.tss.jpa.entity.Instructor;
import com.tss.jpa.exception.StudentApiException;

@Service
public class InstructoreServiceImp implements InstructorService {

	@Autowired
	private InstructorRepository response;

	@Autowired
	private CourseRepositary cresponse;

	@Autowired
	private ModelMapper mapper;

	@Override
	public InstructorRepDto addNewInstructor(InstructoresReqDto dto) {
		Instructor instructor = mapper.map(dto, Instructor.class);
		Instructor dbInstructor = response.save(instructor);
		return mapper.map(dbInstructor, InstructorRepDto.class);
	}

	@Override
	public InstructorRepDto assignCourse(long instructorId, long courseId) {

		Instructor instructor = response.findById(instructorId)
				.orElseThrow(() -> new StudentApiException("Instructor not found"));

		List<Course> dbCourses = instructor.getCourse();

		Course course = cresponse.findById(courseId).orElseThrow(() -> new StudentApiException("not found"));

		course.setInstructor(instructor);

		Course updateCourse = cresponse.save(course);
		dbCourses.add(course);

		instructor.setCourse(dbCourses);

		Instructor updateInstructor = response.save(instructor);

		response.save(instructor);

		return mapper.map(updateInstructor, InstructorRepDto.class);
	}

	public List<InstructorRepDto> fetchAllInstructors() {
		List<Instructor> instructors = response.findAll();
		return instructors.stream().map(instructor -> mapper.map(instructor, InstructorRepDto.class))
				.collect(Collectors.toList());
	}

	
	public List<CourseResDto> fetchCoursesOfInstructor(long instructorId) {
		Instructor instructor = response.findById(instructorId)
				.orElseThrow(() -> new StudentApiException("Instructor not found"));

		List<Course> courses = instructor.getCourse();
		return courses.stream().map(course -> mapper.map(course, CourseResDto.class)).collect(Collectors.toList());
	}

	
	public InstructorRepDto fetchInstructorOfCourse(long courseId) {
		Course course = cresponse.findById(courseId).orElseThrow(() -> new StudentApiException("Course not found"));

		Instructor instructor = course.getInstructor();
		if (instructor == null) {
			throw new StudentApiException("No instructor assigned to this course");
		}

		return mapper.map(instructor, InstructorRepDto.class);
	}

	
	public void removeCourseFromInstructor(long instructorId, long courseId) {
		Instructor instructor = response.findById(instructorId)
				.orElseThrow(() -> new StudentApiException("Instructor not found"));

		Course course = cresponse.findById(courseId).orElseThrow(() -> new StudentApiException("Course not found"));

		if (!instructor.getCourse().contains(course)) {
			throw new StudentApiException("Course not assigned to this instructor");
		}

		course.setInstructor(null);
		cresponse.save(course);

		List<Course> dbCourses = instructor.getCourse();
		dbCourses.remove(course);
		instructor.setCourse(dbCourses);
		response.save(instructor);
	}

	@Override
	public InstructorRepDto assignCourses(long instructorId, List<Long> courseIds) {

		Instructor instructor = response.findById(instructorId)
				.orElseThrow(() -> new StudentApiException("Instructor not found"));

		List<Course> dbCourses = instructor.getCourse();

		for (Long courseId : courseIds) {
			Course course = cresponse.findById(courseId)
					.orElseThrow(() -> new StudentApiException("Course not found with id " + courseId));

			course.setInstructor(instructor);
			cresponse.save(course);

			if (!dbCourses.contains(course)) {
				dbCourses.add(course);
			}
		}

		instructor.setCourse(dbCourses);
		Instructor updatedInstructor = response.save(instructor);

		return mapper.map(updatedInstructor, InstructorRepDto.class);
	}

}
