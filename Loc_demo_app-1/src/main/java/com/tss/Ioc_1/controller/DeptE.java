package com.tss.Ioc_1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.Ioc_1.entity.Dept;

@RestController
@RequestMapping("/app")
public class DeptE {
	@Autowired()
	private Dept dept;
	@GetMapping("/dept")
	public Dept getDept() {
		
		return dept;
		
	}
	
	

}
