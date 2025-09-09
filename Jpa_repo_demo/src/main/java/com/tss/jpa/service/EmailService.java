package com.tss.jpa.service;

import com.tss.jpa.entity.EmailDetail;

public interface EmailService {
	
	   String sendSimpleMail(EmailDetail details);

	 
	    String sendMailWithAttachment(EmailDetail details);

}
