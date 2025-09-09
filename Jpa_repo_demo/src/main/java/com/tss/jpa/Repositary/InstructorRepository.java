package com.tss.jpa.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tss.jpa.entity.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
	

}
