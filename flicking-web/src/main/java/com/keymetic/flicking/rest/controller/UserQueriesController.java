package com.keymetic.flicking.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.keymetic.flicking.core.entity.User;
import com.keymetic.flicking.core.events.UserDetailsEvent;
import com.keymetic.flicking.core.events.UserListEvent;
import com.keymetic.flicking.core.exception.UserNotFoundException;
import com.keymetic.flicking.core.service.UserService;
import com.keymetic.flicking.core.vo.UserVO;

@Controller
@RequestMapping("/api/users")
public class UserQueriesController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserVO>> getUsers(@RequestParam(defaultValue = "10") int limit,@RequestParam(defaultValue = "0") int offset) {
		
		UserListEvent userListEvent = userService.getUsers(limit, offset);
		
		if (!userListEvent.isEntityFound()) {
			return new  ResponseEntity<List<UserVO>>(HttpStatus.NOT_FOUND);
		}
		
		List<UserVO> usersResult = null;
		List<User> users = userListEvent.getUsers();
		
		if(users != null){
			usersResult = UserVO.toUserVOList(users);
		}		
		
		return new ResponseEntity<List<UserVO>>(usersResult, HttpStatus.OK);
	}


	@RequestMapping(method = RequestMethod.GET, value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserVO> viewUser(@PathVariable Long id) {

		UserDetailsEvent details = userService.getUserById(id);
		
		if (!details.isEntityFound()) {
			throw new UserNotFoundException(id);
		}

		User user = details.getUser();
		UserVO userVO = null;
		if(user != null){
			userVO = new UserVO(user);
		}

		return new ResponseEntity<UserVO>(userVO, HttpStatus.OK);
	}
	


}
