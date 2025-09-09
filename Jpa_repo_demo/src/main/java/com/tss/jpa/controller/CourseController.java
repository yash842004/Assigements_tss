package com.tss.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tss.jpa.dto.CourseReqDto;
import com.tss.jpa.dto.CourseResDto;
import com.tss.jpa.service.CourseService;

@RestController
@RequestMapping("/studentapp")
public class CourseController {

	@Autowired
	private CourseService courseService;

	@PostMapping("/course")
	public ResponseEntity<CourseResDto> addNewCourse(@RequestBody CourseReqDto course) {
		CourseResDto dto = courseService.addNewCourseCourseReqDto(course);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/courses/{courseId}/instructor")
	public ResponseEntity<CourseResDto> assignInstructor(@PathVariable long courseId, @RequestParam long instructorId) {
		CourseResDto dto = courseService.assignInstructor(courseId, instructorId);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/courses/{courseId}/fees")
	public ResponseEntity<CourseResDto> updateCourseFees(@PathVariable long courseId, @RequestParam double fees) {
		CourseResDto dto = courseService.updateCourseFees(courseId, fees);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/courses/{CourseId}/students") 
	public ResponseEntity<CourseResDto> assignStudent(@PathVariable long CourseId, @RequestParam int StudentId) {

		return ResponseEntity.ok(courseService.assignStudent(CourseId, StudentId));
	}

}
