package com.keymetic.flicking.core.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@XmlRootElement
public class ContactVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="Name Should Not Be Empty Or Null.")
	private String name;
	
	@NotEmpty(message="Email/Username Should Not Be Empty Or Null.")
	@Email(message="Email Should Be In Valid Format.")
	private String email;
	
	private String phone;
	
	@NotEmpty(message="Subject Should Not Be Empty Or Null.")
	private String subject;
	
	@NotEmpty(message="Message Should Not Be Empty Or Null.")
	private String message;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
	

}
