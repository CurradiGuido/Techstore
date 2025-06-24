package it.uniroma3.siw_techstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail(String fromName, String fromMail, String messageText) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("guidocurradi1998@gmail.com");
		message.setSubject("Nuovo messaggio da " + fromName);
		message.setText("Email: " + fromMail + "\n\nMessaggio:\n" + messageText);
		mailSender.send(message);
	}

}
