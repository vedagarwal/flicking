package com.keymetic.flicking.core.vo;

import java.io.Serializable;

public class FieldErrorInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fieldName;
	private String fieldError;
	
	public FieldErrorInfo(String fieldName, String fieldError) {
		this.fieldName = fieldName;
		this.fieldError = fieldError;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldError() {
		return fieldError;
	}

	public void setFieldError(String fieldError) {
		this.fieldError = fieldError;
	}
	
	
	
	

}
