package com.keymetic.flicking.core.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keymetic.flicking.core.dao.UserDao;
import com.keymetic.flicking.core.entity.User;
import com.keymetic.flicking.core.events.UserCreatedEvent;
import com.keymetic.flicking.core.events.UserDeletedEvent;
import com.keymetic.flicking.core.events.UserDetailsEvent;
import com.keymetic.flicking.core.events.UserListEvent;
import com.keymetic.flicking.core.events.UserUpdatedEvent;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
		
	public UserCreatedEvent addUser(User user){
		userDao.addUser(user);
		return new UserCreatedEvent(user);
	}	
	
	public UserDetailsEvent getUserById(Long id){
		
		User user = userDao.getUserById(id);
		
		if(user == null){
			return UserDetailsEvent.notFound();
		}
		
		return new UserDetailsEvent(user);
	}
	
	public UserDetailsEvent getUserByEmail(String email){
		
		User user = userDao.getUserByEmail(email);
		
		if(user == null){
			return UserDetailsEvent.notFound();
		}
		
		return new UserDetailsEvent(user);
	}
	
	
	public UserUpdatedEvent updateUser(User user){
		User uUser = userDao.getUserById(user.getId());

		if(uUser == null){
			return UserUpdatedEvent.notFound();
		}
		
		user = userDao.updateUser(user);
		return new UserUpdatedEvent(user);
		
	}
	
	
	public UserUpdatedEvent disableEnableUser(Long id,boolean value){
		User uUser = userDao.getUserById(id);

		if(uUser == null){
			return UserUpdatedEvent.notFound();
		}
		
		User user = userDao.disableEnableUser(id, value);
		return new UserUpdatedEvent(user);
	}
	
	public UserUpdatedEvent updatePassword(Long id,String password){
		User uUser = userDao.getUserById(id);

		if(uUser == null){
			return UserUpdatedEvent.notFound();
		}
		
		User user = userDao.updatePassword(id, password);
		return new UserUpdatedEvent(user);
	}
	
	
	public UserDeletedEvent deleteUser(Long id){
		User user = userDao.getUserById(id);
		
		if(user == null){
			return UserDeletedEvent.notFound();
		}
		
		//copying bean before deleting
		User user2 = new User();
		BeanUtils.copyProperties(user, user2);
		
		userDao.deleteUser(id);
		
		return new UserDeletedEvent(user2);
	}
	
	
	public UserListEvent getUsers(int limit, int offset){
		List<User> users = userDao.getUsers(limit, offset);		
		
		if(users == null || users.size() == 0 ){
			return UserListEvent.notFound();
		}
		
		return new UserListEvent(users);
	}


}
