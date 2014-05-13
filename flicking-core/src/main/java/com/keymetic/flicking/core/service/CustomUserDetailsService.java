package com.keymetic.flicking.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keymetic.flicking.core.dao.UserDao;
import com.keymetic.flicking.core.entity.User;
import com.keymetic.flicking.core.vo.AppUserDetails;
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {
		User domainUser = userDao.getUserByEmail(email);
		if(domainUser == null){
			throw new UsernameNotFoundException("Invalid username/password");
		}		
		return new AppUserDetails(domainUser);
	}

}
