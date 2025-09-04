package com.oms.notification_service.emailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	 @Autowired
     private JavaMailSender mailSender;
	 
	 public void sendNotification(String to, String subject, String message) {
	        SimpleMailMessage email = new SimpleMailMessage();
	        email.setTo(to);
	        email.setSubject(subject);
	        email.setText(message);
	        email.setFrom("swathi.sender.test@gmail.com");

	        mailSender.send(email);
	    }

}
