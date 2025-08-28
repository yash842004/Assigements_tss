package com.tss.Ioc_1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tss.Ioc_1.entity.Dept;
import com.tss.Ioc_1.entity.Employee;

@Configuration
public class AddConfig {

	@Bean
	Dept dept() {
		return new Dept();
	}


	@Bean
	Employee employee() {
		return new Employee();
	}

}
