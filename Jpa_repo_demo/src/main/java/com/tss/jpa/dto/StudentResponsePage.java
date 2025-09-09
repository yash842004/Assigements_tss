package com.tss.jpa.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
@AllArgsConstructor
public class StudentResponsePage {

	public StudentResponsePage(List<StudentResponseDto> studentDtos, int number, long totalElements2, int totalPages) {
		
	}

	private List<StudentResponseDto> students;
	private int currentPage;
	private int totalPages;
	private long totalElements;

}