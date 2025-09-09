package com.tss.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tss.jpa.dto.EmailRequest;

@RestController
public class EmailController {

	  @Autowired
	    private JavaMailSender mailSender;

	    @PostMapping("/sendMail")
	    public String sendMail(@RequestBody EmailRequest request) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(request.getTo());
	        message.setSubject(request.getSubject());
	        message.setText(request.getBody());
	        mailSender.send(message);

	        return "Mail sent successfully to " + request.getTo();
	    }
}
