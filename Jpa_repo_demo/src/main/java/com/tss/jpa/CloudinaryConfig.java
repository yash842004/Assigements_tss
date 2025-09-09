package com.tss.jpa;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap("cloud_name", "dzlp6yy6y", "api_key", "554433782188911", "api_secret",
				"QI7ADiVjeI8Nt5eAmzu2keJ9Rz0"));
	}

}
