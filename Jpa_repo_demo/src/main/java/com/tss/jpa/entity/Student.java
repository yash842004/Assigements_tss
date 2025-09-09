package com.tss.jpa.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "students")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter // -> @Data
@Setter // -> @Data
@ToString // -> @Data
public class Student {

	@Id // primary key
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int studentId;
	@Column
	private int rollNumber;
	@Column
	private String firstName;
	@Column
	private String lastName;
	@Column
	private String email;
	@Column
	private int age;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	private Address address;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "student-course", joinColumns = @JoinColumn(name = "student_id"),

			inverseJoinColumns = @JoinColumn(name = "course_id"))
	private List<Course> courses;

}
