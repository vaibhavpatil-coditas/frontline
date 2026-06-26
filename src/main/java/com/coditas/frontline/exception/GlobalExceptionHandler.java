package com.coditas.frontline.exception;

import com.coditas.frontline.constants.ExceptionMessage;
import com.coditas.frontline.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.io.IOException;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorResponse> multipartExceptionHandler(MultipartException exception){
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.FILE_NOT_ATTACHED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> iOExceptionHandler(IOException exception){
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> expiredJwtExceptionHandler(ExpiredJwtException exception){
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.JWT_TOKEN_EXPIRED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResourceMismatchedException.class)
    public ResponseEntity<ErrorResponse> resourceMismatchedExceptionHandler(ResourceMismatchedException exception){
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> resourceAlreadyExistsExceptionHandler(ResourceAlreadyExistsException exception){
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.UNPROCESSABLE_CONTENT.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(NotFoundException exception){
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentialsExceptionHandler(BadCredentialsException exception){
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Enter valid email and password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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
