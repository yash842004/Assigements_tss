package com.tss.hello_spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	@GetMapping("/hello")
	public String displayHello() {
		return "Hello Everyone";
	}
	@GetMapping("/bye")
	public String displayBye() {
		return "Good bye";
	}
	
}
