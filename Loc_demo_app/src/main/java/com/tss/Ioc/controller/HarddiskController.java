package com.tss.Ioc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.Ioc.entity.Harddisk;

@RestController
@RequestMapping("/app") 
public class HarddiskController {
	@Autowired
	private Harddisk harddisk;
	@GetMapping("/harddisk")
	public Harddisk getHarddisk() {
		return  harddisk;
		
	}

}
