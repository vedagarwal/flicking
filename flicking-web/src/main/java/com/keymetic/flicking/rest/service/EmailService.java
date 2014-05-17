package com.keymetic.flicking.rest.service;


import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.keymetic.flicking.core.vo.ContactVO;
import com.keymetic.flicking.core.vo.UserVO;

@Service
public class EmailService {

	@Autowired 
	private JavaMailSender mailSender;

	@Autowired 
	private TemplateEngine templateEngine;

	@Value("${mail.from.email}")
	private String fromEmail;
	
	@Value("${mail.from.name}")
	private String fromName;


	@Resource(name = "messageProperties")
	private Properties messageProperties;
	
	private static final Logger logger = Logger.getLogger(EmailService.class);



	/* 
	 * Send HTML mail (simple) 
	 */
	public void sendSimpleMail(
			final String recipientName, final String recipientEmail, final Locale locale) 
					throws MessagingException {

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("name", recipientName);
		ctx.setVariable("subscriptionDate", new Date());
		ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
		message.setSubject("Example HTML email (simple)");
		message.setFrom("thymeleaf@example.com");
		message.setTo(recipientEmail);

		// Create the HTML body using Thymeleaf
		final String htmlContent = this.templateEngine.process("email-registration.html", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Send email
		this.mailSender.send(mimeMessage);

	}




	/* 
	 * Send HTML mail with attachment. 
	 */
	public void sendMailWithAttachment(
			final String recipientName, final String recipientEmail, final String attachmentFileName, 
			final byte[] attachmentBytes, final String attachmentContentType, final Locale locale) 
					throws MessagingException {

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("name", recipientName);
		ctx.setVariable("subscriptionDate", new Date());
		ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = 
				new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
		message.setSubject("Example HTML email with attachment");
		message.setFrom("thymeleaf@example.com");
		message.setTo(recipientEmail);

		// Create the HTML body using Thymeleaf
		final String htmlContent = this.templateEngine.process("email-withattachment.html", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Add the attachment
		final InputStreamSource attachmentSource = new ByteArrayResource(attachmentBytes);
		message.addAttachment(
				attachmentFileName, attachmentSource, attachmentContentType);

		// Send mail
		this.mailSender.send(mimeMessage);

	}



	/* 
	 * Send HTML mail with inline image
	 */
	public void sendMailWithInline(
			final String recipientName, final String recipientEmail, final String imageResourceName, 
			final byte[] imageBytes, final String imageContentType, final Locale locale)
					throws MessagingException {

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("name", recipientName);
		ctx.setVariable("subscriptionDate", new Date());
		ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
		ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = 
				new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
		message.setSubject("Example HTML email with inline image");
		message.setFrom("thymeleaf@example.com");
		message.setTo(recipientEmail);

		// Create the HTML body using Thymeleaf
		final String htmlContent = this.templateEngine.process("email-inlineimage.html", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
		final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
		message.addInline(imageResourceName, imageSource, imageContentType);

		// Send mail
		this.mailSender.send(mimeMessage);

	}




	public void sendMailOnContact(ContactVO contactVO,final Locale locale) throws MessagingException {

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("name", contactVO.getName());
		ctx.setVariable("email", contactVO.getEmail());
		ctx.setVariable("phone", contactVO.getPhone());
		ctx.setVariable("subject",contactVO.getSubject());
		ctx.setVariable("message",contactVO.getMessage());

		// Sending response to site admin
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
		message.setSubject(messageProperties.getProperty("contact.mail.subject"));
		message.setFrom(this.fromEmail);
		message.setTo(this.fromEmail);

		// Create the HTML body using Thymeleaf
		final String htmlContent = this.templateEngine.process("email-contact.html", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Send email
		this.mailSender.send(mimeMessage);


		// Sending auto response to contact person
		final MimeMessage mimeMessageTo = this.mailSender.createMimeMessage();
		final MimeMessageHelper messageTo = new MimeMessageHelper(mimeMessageTo, "UTF-8");
		messageTo.setSubject(messageProperties.getProperty("contact.auto.mail.subject"));
		
		try {
			messageTo.setFrom(new InternetAddress(this.fromEmail,this.fromName));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		
		messageTo.setTo(contactVO.getEmail());

		// Create the HTML body using Thymeleaf
		final String htmlContentTo = this.templateEngine.process("email-contact-auto-response.html", ctx);
		messageTo.setText(htmlContentTo, true /* isHtml */);

		// Send email
		this.mailSender.send(mimeMessageTo);


	}
	
	
	public void sendMailOnRegistration(UserVO userVO,final Locale locale) throws MessagingException {

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("name", userVO.getFirstName());
		ctx.setVariable("email", userVO.getEmail());
		
		// Sending response to new registered user
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
		message.setSubject(messageProperties.getProperty("registration.mail.subject"));
		try {
			message.setFrom(new InternetAddress(this.fromEmail,this.fromName));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		message.setTo(userVO.getEmail());

		// Create the HTML body using Thymeleaf
		final String htmlContent = this.templateEngine.process("email-registration.html", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Send email
		this.mailSender.send(mimeMessage);

	}
	
	public void sendMailOnForgotPassword(UserVO userVO,String newPassword,final Locale locale) throws MessagingException {

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("name", userVO.getFirstName());
		ctx.setVariable("email", userVO.getEmail());
		ctx.setVariable("password", newPassword);
		
		// Sending response to new registered user
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
		message.setSubject(messageProperties.getProperty("forgotpassword.mail.subject"));
		try {
			message.setFrom(new InternetAddress(this.fromEmail,this.fromName));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		message.setTo(userVO.getEmail());

		// Create the HTML body using Thymeleaf
		final String htmlContent = this.templateEngine.process("email-forgot-password.html", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Send email
		this.mailSender.send(mimeMessage);

	}


}