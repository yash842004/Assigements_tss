package com.tss.jpa.service;

import java.util.List;

import com.tss.jpa.dto.CourseResDto;
import com.tss.jpa.dto.InstructorRepDto;
import com.tss.jpa.dto.InstructoresReqDto;

public interface InstructorService {

	InstructorRepDto addNewInstructor(InstructoresReqDto dto);
	
	InstructorRepDto assignCourse(long instructorId, long courseId); 
	List<InstructorRepDto> fetchAllInstructors();  
    List<CourseResDto> fetchCoursesOfInstructor(long instructorId);  
    void removeCourseFromInstructor(long instructorId, long courseId);  
    InstructorRepDto fetchInstructorOfCourse(long courseId);
    InstructorRepDto assignCourses(long instructorId, List<Long> courseIds);

}
