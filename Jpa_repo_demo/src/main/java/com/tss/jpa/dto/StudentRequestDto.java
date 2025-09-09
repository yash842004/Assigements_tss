package com.tss.jpa.dto;

import com.tss.jpa.entity.Address;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class StudentRequestDto {

    @Min(value = 1, message = "Roll number must be greater than 0")
	private int rollNumber;

    @NotBlank(message = "First name cannot be empty")
	private String firstName;

    @NotBlank(message = "Last name cannot be empty")
	private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
	private String email;

    @Min(value = 18, message = "Age must be at least 18")
	private int age;
    
    private Address address;

}
