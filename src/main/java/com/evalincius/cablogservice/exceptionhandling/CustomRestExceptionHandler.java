package com.evalincius.cablogservice.exceptionhandling;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.evalincius.cablogservice.models.ApiError;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,HttpStatusCode status, WebRequest request) {

    List<String> errors = new ArrayList<String>();
    for (FieldError error : exception.getBindingResult().getFieldErrors()) {
        errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (ObjectError error : exception.getBindingResult().getGlobalErrors()) {
        errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), errors);
      log.error( "Validation Error:", exception );
    return handleExceptionInternal(exception, apiError, headers, apiError.getStatus(), request);
  }

  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public final ResponseEntity<Object> handleNotFoundException(SQLIntegrityConstraintViolationException ex, WebRequest request) {
      ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(),  ex.getMessage());
      log.error( "Databse Error:", ex );
      return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }
  
}
