package com.bank_management_system.bank_project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourseException(DuplicateResourceException e) {
        ErrorResponse res = new ErrorResponse();
        
        res.setStatusCode(HttpStatus.CONFLICT.value());
        res.setMessage(e.getMessage());
        
        return new ResponseEntity<ErrorResponse>(res, HttpStatus.CONFLICT);

    }
	
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataException(InvalidDataException e) {
        ErrorResponse res = new ErrorResponse();
        
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(e.getMessage());
        
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }
    
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
		ErrorResponse res = new ErrorResponse();
		
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		res.setMessage(e.getMessage());
		
		return new ResponseEntity<ErrorResponse>(res,HttpStatus.NOT_FOUND);
	}
	
	//this exception is handled by ResponseEntityExceptionHandler which have extended our GlobalExceptionHandler
	//so if we try to handle it we need to @Override predefined handler
	
//	@ExceptionHandler(HttpMessageNotReadableException.class)
//	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
//		ErrorResponse res = new ErrorResponse();
//		
//		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
//		
//		String detailMessage = (e.getMostSpecificCause()!=null)?e.getMostSpecificCause().getMessage():e.getMessage();
//		res.setMessage("Invalid JSON input: "+detailMessage);
//		
//		return new ResponseEntity<ErrorResponse>(res, HttpStatus.BAD_REQUEST);
//	}/
	
	@ExceptionHandler(InsufficientInitialBalanceException.class)
	public ResponseEntity<ErrorResponse> handleInsufficientInitialBalanceException(InsufficientInitialBalanceException e) {
		ErrorResponse res = new ErrorResponse();
		
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		res.setMessage(e.getMessage());
		
		return new ResponseEntity<ErrorResponse>(res, HttpStatus.BAD_REQUEST);
	}
	
}
