package com.keymetic.flicking.core.vo;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.keymetic.flicking.core.entity.User;
import com.keymetic.flicking.core.util.AppUserAuthorityUtils;

public class AppUserDetails extends User implements UserDetails,Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;

	public AppUserDetails(User user){
		this.user = user;
		setId(user.getId());
		setEmail(user.getEmail());
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setPassword(user.getPassword());
		setEnabled(user.isEnabled());
		
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return AppUserAuthorityUtils.createAuthorities(user);
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	
}
