package com.astrapay.controller.advice;

import com.astrapay.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;
@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGeneric(Exception ex) {
        ResponseDto response = new ResponseDto();
        response.setSuccess(false);
        response.setMessage("Internal Service Error,Check the Logs");
        ex.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ResponseDto response = new ResponseDto();
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        response.setSuccess(false);
        response.setMessage("Validation Error");
        response.setData(errors);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}