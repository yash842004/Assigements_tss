package com.tss.Ioc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tss.Ioc.entity.Computer;
import com.tss.Ioc.entity.Harddisk;

@Configuration
public class AddConfig {
	@Bean
	 Computer computer()
	{
		return new Computer();
	}
	@Bean
	Harddisk harddisk() {
		return new Harddisk();
	}

}
