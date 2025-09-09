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

import com.tss.jpa.dto.CourseResDto;
import com.tss.jpa.dto.InstructorRepDto;
import com.tss.jpa.dto.InstructoresReqDto;
import com.tss.jpa.service.InstructorService;

@RestController
@RequestMapping("/studentapp")
public class InstructorController {

	@Autowired
	private InstructorService instructorService;

	@PostMapping("/instructor")
	public ResponseEntity<InstructorRepDto> addNewInstructor(@RequestBody InstructoresReqDto instructor) {
	    InstructorRepDto dto = instructorService.addNewInstructor(instructor);
	    return ResponseEntity.ok(dto);
	}

	@PutMapping("/instructorIs/{instructorId}/courses")
	public ResponseEntity<InstructorRepDto> assignCourses(@PathVariable long instructorId, @RequestParam long courseId ){
		
		return ResponseEntity.ok(instructorService.assignCourse(instructorId, courseId));
	}
	
	@GetMapping("/instructors")
    public ResponseEntity<List<InstructorRepDto>> fetchAllInstructors() {
        return ResponseEntity.ok(instructorService.fetchAllInstructors());
    }
	
	
	
    @GetMapping("/instructors/{instructorId}/courses")
    public ResponseEntity<List<CourseResDto>> fetchCoursesOfInstructor(@PathVariable long instructorId) {
        return ResponseEntity.ok(instructorService.fetchCoursesOfInstructor(instructorId));
    }
    
    
    
 
    @GetMapping("/courses/{courseId}/instructor")
    public ResponseEntity<InstructorRepDto> fetchInstructorOfCourse(@PathVariable long courseId) {
        return ResponseEntity.ok(instructorService.fetchInstructorOfCourse(courseId));
    }
    
 
    @DeleteMapping("/instructors/{instructorId}/courses/{courseId}")
    public ResponseEntity<Void> removeCourseFromInstructor(@PathVariable long instructorId, @PathVariable long courseId) {
        instructorService.removeCourseFromInstructor(instructorId, courseId);
        return ResponseEntity.noContent().build();
    }
    
    
    
    @PutMapping("/instructors/{instructorId}/courses")
    public ResponseEntity<InstructorRepDto> assignMultipleCourses(
            @PathVariable long instructorId,
            @RequestBody List<Long> courseIds) {
        return ResponseEntity.ok(instructorService.assignCourses(instructorId, courseIds));
    }

}
