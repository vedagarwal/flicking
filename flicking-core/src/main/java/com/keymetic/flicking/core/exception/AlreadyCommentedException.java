package com.keymetic.flicking.core.exception;

public class AlreadyCommentedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AlreadyCommentedException(){
		super("You have already commented");
	}

}
