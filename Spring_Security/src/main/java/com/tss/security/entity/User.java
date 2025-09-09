package com.tss.security.entity;

import com.tss.security.entity.Role;     

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;



@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    private String username;
	    
	    private String password;

	    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE})
	    @JoinColumn(name = "role_id", nullable = false)
	    private Role role;
	
	

}
