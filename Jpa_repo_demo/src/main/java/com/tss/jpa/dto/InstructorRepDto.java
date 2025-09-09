package com.tss.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InstructorRepDto {
	private String name;
	private String qualification;
	private int experience;
	private long instructorId;


}
