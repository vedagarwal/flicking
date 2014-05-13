package com.keymetic.flicking.core.vo;

import java.io.Serializable;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

@XmlRootElement
public class PasswordVO implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String oldPassword;
	
	@NotEmpty(message="Password Should Not Be Empty Or Null.")
	@Size(min=6,message="Password Should Be Minimum Six Chars.")
	private String newPassword;
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	

}
