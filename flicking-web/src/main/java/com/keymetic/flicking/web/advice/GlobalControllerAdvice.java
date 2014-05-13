package com.keymetic.flicking.web.advice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.keymetic.flicking.core.exception.AlreadyCommentedException;
import com.keymetic.flicking.core.exception.CommentNotFoundException;
import com.keymetic.flicking.core.exception.EmailAlreadyExistsException;
import com.keymetic.flicking.core.exception.ImageUploadException;
import com.keymetic.flicking.core.exception.MovieNotFoundException;
import com.keymetic.flicking.core.exception.ReviewNotFoundException;
import com.keymetic.flicking.core.exception.UserNotFoundException;
import com.keymetic.flicking.core.vo.ErrorInfo;
import com.keymetic.flicking.core.vo.FieldErrorInfo;



@ControllerAdvice
public class GlobalControllerAdvice {

	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ExceptionHandler(MovieNotFoundException.class)
	public @ResponseBody ErrorInfo handleMovieNotFound(HttpServletRequest request, Exception exception){
		ErrorInfo error = new ErrorInfo(request.getRequestURL().toString(), exception.getMessage());
		return error;
	}	
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ExceptionHandler(ReviewNotFoundException.class)
	public @ResponseBody ErrorInfo handleReviewNotFound(HttpServletRequest request, Exception exception){
		ErrorInfo error = new ErrorInfo(request.getRequestURL().toString(), exception.getMessage());
		return error;
	}	
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ExceptionHandler(CommentNotFoundException.class)
	public @ResponseBody ErrorInfo handleCommentNotFound(HttpServletRequest request, Exception exception){
		ErrorInfo error = new ErrorInfo(request.getRequestURL().toString(), exception.getMessage());
		return error;
	}	
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ExceptionHandler(UserNotFoundException.class)
	public @ResponseBody ErrorInfo handleUserNotFound(HttpServletRequest request, Exception exception){
		ErrorInfo error = new ErrorInfo(request.getRequestURL().toString(), exception.getMessage());
		return error;
	}
	
	@ResponseStatus(value=HttpStatus.NOT_FOUND)
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public @ResponseBody ErrorInfo handleEmailNotFound(HttpServletRequest request, Exception exception){
		ErrorInfo error = new ErrorInfo(request.getRequestURL().toString(), exception.getMessage());
		return error;
	}
	
	@ResponseStatus(value=HttpStatus.CONFLICT)
	@ExceptionHandler(AlreadyCommentedException.class)
	public @ResponseBody ErrorInfo handleAlreadyCommented(HttpServletRequest request, Exception exception){
		ErrorInfo error = new ErrorInfo(request.getRequestURL().toString(), exception.getMessage());
		return error;
	}
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ImageUploadException.class)
	public @ResponseBody ErrorInfo handleImageUploadException(HttpServletRequest request, ImageUploadException exception){
		ErrorInfo error = new ErrorInfo(request.getRequestURL().toString(), exception.getMessage());		
		return error;
	}

	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public @ResponseBody ErrorInfo handleFieldErrors(HttpServletRequest request, MethodArgumentNotValidException exception){
		ErrorInfo error = new ErrorInfo(request.getRequestURL().toString(), "Error Occurred While Saving Details.");
		BindingResult result = exception.getBindingResult();                
		List<FieldError> fieldErrors = result.getFieldErrors();
		error.getFieldErrors().addAll(populateFieldErrors(fieldErrors));
		return error;
	}
	
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({DataAccessException.class,DataIntegrityViolationException.class})
	public @ResponseBody ErrorInfo handleDataAccessException(HttpServletRequest request, Exception exception){
		ErrorInfo error = new ErrorInfo(request.getRequestURL().toString(), exception.getMessage());
		return error;
	}

	public List< FieldErrorInfo > populateFieldErrors(List<FieldError> fieldErrorList) {
		List<FieldErrorInfo> fieldErrors = new ArrayList<FieldErrorInfo>();
		StringBuilder errorMessage = new StringBuilder("");

		for (FieldError fieldError : fieldErrorList) {

			//errorMessage.append(fieldError.getCode()).append(".");
			//errorMessage.append(fieldError.getObjectName()).append(".");
			//errorMessage.append(fieldError.getField());

			//String localizedErrorMsg = localizeErrorMessage(errorMessage.toString());

			fieldErrors.add(new FieldErrorInfo(fieldError.getField(), fieldError.getDefaultMessage()));
			errorMessage.delete(0, errorMessage.capacity());
		}
		return fieldErrors;
	}

	/*public String localizeErrorMessage(String errorCode) {
		Locale locale = LocaleContextHolder.getLocale();
		String errorMessage = messageSource.getMessage(errorCode, null, locale);
		return errorMessage;
	}*/

}
