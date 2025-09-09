package com.tss.jpa.service;

import com.tss.jpa.dto.CourseReqDto;
import com.tss.jpa.dto.CourseResDto;

public interface CourseService {

	CourseResDto addNewCourseCourseReqDto(CourseReqDto courseReqDto);

	CourseResDto assignInstructor(long courseId, long instructorId);

	CourseResDto updateCourseFees(long courseId, double newFees);

	CourseResDto assignStudent(long courseId, int studentId);

}
