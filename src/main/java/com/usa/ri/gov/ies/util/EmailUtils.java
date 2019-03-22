package com.usa.ri.gov.ies.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * This class is used to work with Java Mail API
 * @author Ashok
 *
 */
@Component("emailUtils")
public class EmailUtils {

	@Autowired(required = true)
	private JavaMailSender mailSender;

	/**
	 * This method is used to send Email with Html text
	 * 
	 * @param to
	 * @param subject
	 * @param text
	 */
	public void sendEmail(String to, String subject, String text) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text, true);// IT is html text
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
