package com.tss.Ioc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.Ioc.entity.Computer;

@RestController
@RequestMapping("/app") // start or base mapping.
public class ComputerController {
	@Autowired()
	private Computer computer;
	@GetMapping("/computer")
	public Computer getComputer() {
		return computer;
	}

}
