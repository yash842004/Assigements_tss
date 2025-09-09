package com.tss.jpa.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tss.jpa.entity.Course;

public interface CourseRepositary extends JpaRepository<Course,Long>{

}
