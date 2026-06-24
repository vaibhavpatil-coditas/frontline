package com.coditas.frontline.exception;

import com.coditas.frontline.constants.ExceptionMessage;
import com.coditas.frontline.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> resourceAlreadyExistsExceptionHandler(ResourceAlreadyExistsException exception){
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        FieldError fieldError = exception.getBindingResult().getFieldError();
        assert fieldError != null;
        String defaultMessage = fieldError.getDefaultMessage();
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST.value(), defaultMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> authorizationDeniedExceptionHandler(AuthorizationDeniedException exception){
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.FORBIDDEN.value(), ExceptionMessage.NOT_AUTHORIZED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    public ErrorResponse createErrorResponse(Integer status, String message){
        return ErrorResponse.builder()
                .message(message)
                .timestamp(Instant.now())
                .status(status)
                .build();
    }

}
