package com.keymetic.flicking.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.keymetic.flicking.core.entity.User;
import com.keymetic.flicking.core.entity.Role;

public class AppUserAuthorityUtils {
	
	public static Collection<? extends GrantedAuthority> createAuthorities(User user){

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(Role role:user.getRoles()){
			GrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
			authorities.add(authority);
		}
		return authorities;

	}

}
