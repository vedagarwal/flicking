package com.keymetic.flicking.core.exception;

public class ImageUploadException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ImageUploadException(Long id){
		super("Unable to upload image for movie:"+id);
	}
	
}
