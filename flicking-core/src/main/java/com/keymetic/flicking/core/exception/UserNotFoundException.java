package com.keymetic.flicking.core.exception;

public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException(Long id){
		super("User not found with id:"+id);
	}
	
	public UserNotFoundException(String email){
		super("User not found with email:"+email);
	}

}
