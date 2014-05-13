package com.keymetic.flicking.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.keymetic.flicking.core.entity.Role;
import com.keymetic.flicking.core.entity.User;
import com.keymetic.flicking.core.util.AppUserRole;

public class UserVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@NotEmpty(message="Email/Username Should Not Be Empty Or Null.")
	@Email(message="Email Should Be In Valid Format.")
	private String email;
	
	@NotEmpty(message="Name Should Not Be Empty Or Null.")
	private String firstName;
	private String lastName;
	private boolean enabled;
	
	@NotEmpty(message="Password Should Not Be Empty Or Null.")
	@Size(min=6,message="Password Should Be Mix 6 Chars.")
	private String password;
	
	private String role;
	
	public UserVO() {
		
	}
	
	
	public UserVO(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.enabled = user.isEnabled();
		this.role = getRole(user);
		
		//this.password = user.getPassword();				
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}


	public User toUser(){
		User user = new User();
		user.setId(this.id);
		user.setEmail(this.email);
		user.setFirstName(this.firstName);
		user.setLastName(this.lastName);
		user.setPassword(this.password);
		user.setEnabled(this.enabled);		
		return user;
	}
	
	public static List<UserVO> toUserVOList(List<User> users){
		List<UserVO> userVOList = new ArrayList<UserVO>();
		
		for(User u:users){
			UserVO uVO = new UserVO(u);
			userVOList.add(uVO);
		}
		
		return userVOList;
	}
	
	private String getRole(User user){
		List<Role> roles = user.getRoles();
		
		for(Role r:roles){
			if(r.getRole().equals(AppUserRole.ROLE_SUPER_ADMIN.toString())){
				return AppUserRole.ROLE_SUPER_ADMIN.toString();
			}else if(r.getRole().equals(AppUserRole.ROLE_ADMIN.toString())){
				return AppUserRole.ROLE_ADMIN.toString();
			}else{
				return AppUserRole.ROLE_USER.toString();
			}
		}
		
		return "ROLE_UNKNOWN";
		
	}	

}
