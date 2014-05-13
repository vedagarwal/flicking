package com.keymetic.flicking.core.exception;

public class CommentNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommentNotFoundException(Long id){
		super("Comment not found with id:"+id);
	}
	
}
