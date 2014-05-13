package com.keymetic.flicking.rest.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.keymetic.flicking.core.entity.Role;
import com.keymetic.flicking.core.entity.User;
import com.keymetic.flicking.core.events.UserCreatedEvent;
import com.keymetic.flicking.core.events.UserDeletedEvent;
import com.keymetic.flicking.core.events.UserUpdatedEvent;
import com.keymetic.flicking.core.exception.UserNotFoundException;
import com.keymetic.flicking.core.service.UserService;
import com.keymetic.flicking.core.util.AppUserRole;
import com.keymetic.flicking.core.vo.PasswordVO;
import com.keymetic.flicking.core.vo.UserVO;
import com.keymetic.flicking.web.security.SaltedSHA256PasswordEncoder;

@Controller
@RequestMapping("/api/users")
public class UserCommandsController {

	@Autowired
	private UserService userService;

	@Autowired
	private SaltedSHA256PasswordEncoder passwordEncoder;


	@RequestMapping(method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserVO> createUser(@RequestBody UserVO userVO, UriComponentsBuilder builder){
		User user = userVO.toUser();
		
		Role role = new Role();
		role.setRole(AppUserRole.ROLE_ADMIN.toString());
		role.setUser(user);
		
		List<Role> roles = new ArrayList<Role>();
		roles.add(role);

		user.setRoles(roles);
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);		

		UserCreatedEvent userCreatedEvent = userService.addUser(user);

		UserVO createdUserVO = null;
		if(userCreatedEvent.getUser() != null){
			createdUserVO = new UserVO(userCreatedEvent.getUser());
		}

		return new ResponseEntity<UserVO>(createdUserVO,null, HttpStatus.CREATED);
	}


	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserVO> deleteUser(@PathVariable Long id) {

		UserDeletedEvent userDeletedEvent = userService.deleteUser(id);

		if (!userDeletedEvent.isEntityFound()) {
			throw new UserNotFoundException(id);
		}

		User user = userDeletedEvent.getUser();
		UserVO userVO = null;
		if(user != null){
			userVO = new UserVO(user);
		}

		if (userDeletedEvent.isDeletionCompleted()) {
			return new ResponseEntity<UserVO>(userVO, HttpStatus.OK);
		}

		return new ResponseEntity<UserVO>(userVO, HttpStatus.FORBIDDEN);
	}


	@RequestMapping(value="/{id}",method = RequestMethod.PUT,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserVO> updateUser(@PathVariable Long id,@RequestBody @Valid UserVO userVO) {    	
		User user = userVO.toUser();		
		
		UserUpdatedEvent userUpdatedEvent = userService.updateUser(user);	    	

		UserVO updatedUserVO = null;
		if(userUpdatedEvent.getUser() != null){
			updatedUserVO = new UserVO(userUpdatedEvent.getUser());
		}
		return new ResponseEntity<UserVO>(updatedUserVO,null, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/{id}/password",method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserVO> updatePassword(@PathVariable Long id,@RequestBody @Valid PasswordVO request) { 
		String encodedPassword = passwordEncoder.encode(request.getNewPassword());
		UserUpdatedEvent userUpdatedEvent = userService.updatePassword(id,encodedPassword);    	
		
		UserVO updatedUserVO = null;
		if(userUpdatedEvent.getUser() != null){
			updatedUserVO = new UserVO(userUpdatedEvent.getUser());
		}
		return new ResponseEntity<UserVO>(updatedUserVO,null, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/{id}/enable",method = RequestMethod.PUT,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserVO> disableEnableUser(@PathVariable Long id,@RequestParam boolean value) {    	
		UserUpdatedEvent userUpdatedEvent = userService.disableEnableUser(id, value);	    	

		UserVO updatedUserVO = null;
		if(userUpdatedEvent.getUser() != null){
			updatedUserVO = new UserVO(userUpdatedEvent.getUser());
		}
		return new ResponseEntity<UserVO>(updatedUserVO,null, HttpStatus.CREATED);
	}
	
	
	
}
