package com.thirdeye3.messagebroker.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.thirdeye3.messagebroker.dtos.Response;
import com.thirdeye3.messagebroker.exceptions.InvalidMachineException;
import com.thirdeye3.messagebroker.exceptions.MessageException;
import com.thirdeye3.messagebroker.exceptions.TopicException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<Response<Void>> handleMessageException(MessageException ex) {
        return buildResponse(false, HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
    }
    
    @ExceptionHandler(TopicException.class)
    public ResponseEntity<Response<Void>> handleTopicException(TopicException ex) {
        return buildResponse(false, HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
    }
    
    @ExceptionHandler(InvalidMachineException.class)
    public ResponseEntity<Response<Void>> handleTopicException(InvalidMachineException ex) {
        return buildResponse(false, HttpStatus.NOT_FOUND.value(), ex.getMessage(), null);
    }
    
    
    

    private <T> ResponseEntity<Response<T>> buildResponse(
            boolean success, int errorCode, String errorMessage, T body) {
        return ResponseEntity
                .status(errorCode)
                .body(new Response<>(success, errorCode, errorMessage, body));
    }
}

