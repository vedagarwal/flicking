package com.keymetic.flicking.core.exception;

public class ReviewNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ReviewNotFoundException(Long id){
		super("Review not found with id:"+id);
	}

}
