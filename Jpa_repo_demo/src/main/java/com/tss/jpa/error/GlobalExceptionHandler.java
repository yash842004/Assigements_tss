package com.tss.jpa.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.tss.jpa.exception.AgeException;
import com.tss.jpa.exception.EmailException;
import com.tss.jpa.exception.FirstNameException;
import com.tss.jpa.exception.LastNameException;
import com.tss.jpa.exception.RollNumberException;
import com.tss.jpa.exception.StudentApiException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidations(MethodArgumentNotValidException exception) {

		Map<String, String> errors = new HashMap<>();

		exception.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RollNumberException.class)
	public ResponseEntity<ResponseError> handleRollNumberException(RollNumberException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(FirstNameException.class)
	public ResponseEntity<ResponseError> handleFirstNameException(FirstNameException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(LastNameException.class)
	public ResponseEntity<ResponseError> handleLastNameException(LastNameException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EmailException.class)
	public ResponseEntity<ResponseError> handleEmailException(EmailException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AgeException.class)
	public ResponseEntity<ResponseError> handleAgeException(AgeException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(StudentApiException.class)
	public ResponseEntity<ResponseError> handleStudentApiException(StudentApiException ex) {
		return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ResponseError> buildErrorResponse(String message, HttpStatus status) {
		ResponseError error = new ResponseError(status.value(), System.currentTimeMillis(), message);
		return new ResponseEntity<>(error, status);
	}

}
