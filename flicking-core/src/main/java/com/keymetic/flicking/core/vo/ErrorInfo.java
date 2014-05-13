package com.keymetic.flicking.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ErrorInfo implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String url;
	private String message;
	private List<FieldErrorInfo> fieldErrors = new ArrayList<FieldErrorInfo>();
	
	public ErrorInfo(String url, String message) {
	
		this.url = url;
		this.message = message;
	}	
	
	
	public ErrorInfo(String url, String message, List<FieldErrorInfo> fieldErrors) {
		this.url = url;
		this.message = message;
		this.fieldErrors = fieldErrors;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public List<FieldErrorInfo> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldErrorInfo> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
	

}
