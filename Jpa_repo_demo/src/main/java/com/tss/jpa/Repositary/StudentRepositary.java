package com.tss.jpa.Repositary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tss.jpa.entity.Student;

@Repository
public interface StudentRepositary extends JpaRepository<Student, Integer> {

	Page<Student> findByFirstName(String firstName, Pageable pageable);
	boolean deleteByFirstName(String name);
	
}