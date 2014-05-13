package com.keymetic.flicking.rest.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.keymetic.flicking.core.entity.Role;
import com.keymetic.flicking.core.entity.User;
import com.keymetic.flicking.core.events.UserCreatedEvent;
import com.keymetic.flicking.core.events.UserDetailsEvent;
import com.keymetic.flicking.core.events.UserUpdatedEvent;
import com.keymetic.flicking.core.exception.EmailAlreadyExistsException;
import com.keymetic.flicking.core.exception.UserNotFoundException;
import com.keymetic.flicking.core.service.UserService;
import com.keymetic.flicking.core.util.AppUserRole;
import com.keymetic.flicking.core.vo.ContactVO;
import com.keymetic.flicking.core.vo.ForgotPasswordVO;
import com.keymetic.flicking.core.vo.PasswordVO;
import com.keymetic.flicking.core.vo.UserVO;
import com.keymetic.flicking.rest.service.EmailService;
import com.keymetic.flicking.web.security.SaltedSHA256PasswordEncoder;
import com.keymetic.flicking.web.security.TokenUtils;
import com.keymetic.flicking.web.transfer.TokenTransfer;
import com.keymetic.flicking.web.transfer.UserTransfer;

@Controller
@RequestMapping("/api/user")
public class LoginController {

	@Autowired
	private UserDetailsService userService;

	@Autowired
	private UserService uService;

	@Autowired
	private EmailService emailService;

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;

	@Autowired
	private SaltedSHA256PasswordEncoder passwordEncoder;



	/**
	 * Retrieves the currently logged in user.
	 * 
	 * @return A transfer containing the username and the roles.
	 */

	@RequestMapping(method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserTransfer> getUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
			//throw new WebApplicationException(401);
			return new ResponseEntity<UserTransfer>(HttpStatus.UNAUTHORIZED);
		}
		UserDetails userDetails = (UserDetails) principal;
		UserTransfer userTransfer = new UserTransfer(userDetails.getUsername(), this.createRoleMap(userDetails));

		return new ResponseEntity<UserTransfer>(userTransfer,HttpStatus.OK);
	}


	/**
	 * Authenticates a user and creates an authentication token.
	 * 
	 * @param username
	 *            The name of the user.
	 * @param password
	 *            The password of the user.
	 * @return A transfer containing the authentication token.
	 */

	@RequestMapping(value="/authenticate",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody TokenTransfer authenticate(@RequestParam("username") String username, @RequestParam("password") String password) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		Authentication authentication = this.authManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		/*
		 * Reload user as password of authentication principal will be null after authorization and
		 * password is needed for token generation
		 */
		UserDetails userDetails = this.userService.loadUserByUsername(username);

		return new TokenTransfer(TokenUtils.createToken(userDetails));
	}

	@RequestMapping(value="/register",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserVO> register(@RequestBody @Valid UserVO userVO,final Locale locale) {

		UserDetailsEvent userDetailsEvent = uService.getUserByEmail(userVO.getEmail());

		if(userDetailsEvent.isEntityFound()){
			throw new EmailAlreadyExistsException(userVO.getEmail());
		}

		User user = userVO.toUser();

		Role role = new Role();
		role.setRole(AppUserRole.ROLE_USER.toString());
		role.setUser(user);

		List<Role> roles = new ArrayList<Role>();
		roles.add(role);

		user.setRoles(roles);
		user.setEnabled(true);

		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);		

		UserCreatedEvent userCreatedEvent = uService.addUser(user);

		UserVO createdUserVO = null;
		if(userCreatedEvent.getUser() != null){
			createdUserVO = new UserVO(userCreatedEvent.getUser());
			try {
				emailService.sendMailOnRegistration(userVO, locale);
			} catch (MessagingException e) {				
				e.printStackTrace();
			}
		}

		return new ResponseEntity<UserVO>(createdUserVO,null, HttpStatus.CREATED);
	}


	private Map<String, Boolean> createRoleMap(UserDetails userDetails) {

		Map<String, Boolean> roles = new HashMap<String, Boolean>();
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			roles.put(authority.getAuthority(), Boolean.TRUE);
		}

		return roles;
	}

	@RequestMapping(value="/forgotpassword",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordVO forgotPasswordVO,final Locale locale){		
		UserDetailsEvent details = uService.getUserByEmail(forgotPasswordVO.getEmail());

		if (!details.isEntityFound()) {
			throw new UserNotFoundException(forgotPasswordVO.getEmail());
		}
		String password = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
		String encodedPassword = passwordEncoder.encode(password);
		UserUpdatedEvent userUpdatedEvent = uService.updatePassword(details.getUser().getId(), encodedPassword);   	

		UserVO updatedUserVO = null;
		if(userUpdatedEvent.getUser() != null){
			updatedUserVO = new UserVO(userUpdatedEvent.getUser());
			
			try {
				emailService.sendMailOnForgotPassword(updatedUserVO, password, locale);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}


		return new ResponseEntity<String>("Password Reset Successful",null, HttpStatus.CREATED);
	}	

	@RequestMapping(value="/contact",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> contact(@RequestBody @Valid ContactVO contactVO,final Locale locale) throws MessagingException {

		emailService.sendMailOnContact(contactVO, locale);

		return new ResponseEntity<String>("Email Sent Successfully",null, HttpStatus.CREATED);
	}





}
