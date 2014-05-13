package com.keymetic.flicking.core.exception;

public class EmailAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EmailAlreadyExistsException(String email){
		super("Email "+email+" is already registered.");
	}

}
