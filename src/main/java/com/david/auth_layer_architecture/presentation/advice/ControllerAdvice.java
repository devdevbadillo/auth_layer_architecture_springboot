package com.david.auth_layer_architecture.presentation.advice;

import java.util.Map;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.david.auth_layer_architecture.domain.exceptions.auth.HasAccessWithOAuth2Exception;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserNotVerifiedException;
import com.david.auth_layer_architecture.domain.exceptions.credential.UserNotFoundException;
import com.david.auth_layer_architecture.presentation.messages.EmailMessages;
import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.david.auth_layer_architecture.domain.exceptions.credential.UserAlreadyExistException;

@RestControllerAdvice
public class ControllerAdvice {
    
    private static final String KEY_MESSAGE = "message";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<Map<String, String>> handleValidationErrors(
        MethodArgumentNotValidException ex
    ) {

        String error = ex.getBindingResult()
                .getFieldErrors()
                .stream().map(FieldError::getDefaultMessage)
                .findFirst().orElse(ex.getMessage());

        return new ResponseEntity<>(Map.of(KEY_MESSAGE, error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    private ResponseEntity<Map<String, String>> handleMissingRequestHeaderException(
        MissingRequestHeaderException ex
    ){
        return new ResponseEntity<>(Map.of(KEY_MESSAGE, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<Map<String, String>> handleConstraintViolationException(
        ConstraintViolationException ex
    ){
        return new ResponseEntity<>(Map.of(KEY_MESSAGE, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    private ResponseEntity<Map<String, String>> handleUserAlreadyExistException(
        UserAlreadyExistException ex
    ){
        return new ResponseEntity<>(Map.of(KEY_MESSAGE, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<Map<String, String>> handleBadCredentialsException(
        BadCredentialsException ex
    ){
        return new ResponseEntity<>(Map.of(KEY_MESSAGE, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JWTVerificationException.class)
    private ResponseEntity<Map<String, String>> handleJWTVerificationException(JWTVerificationException ex){
        return new ResponseEntity<>(Map.of(KEY_MESSAGE, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HasAccessWithOAuth2Exception.class)
    private ResponseEntity<Map<String, String>> handleHaveAccessWithOAuth2Exception(HasAccessWithOAuth2Exception ex){
        return new ResponseEntity<>(Map.of(KEY_MESSAGE, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler( UserNotVerifiedException.class)
    private ResponseEntity<Map<String, String>> handleUserNotVerifiedException(UserNotVerifiedException ex){
        return new ResponseEntity<>(Map.of(KEY_MESSAGE, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex){
        return new ResponseEntity<>(Map.of(KEY_MESSAGE, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MailException.class)
    private ResponseEntity<Map<String, String>> handleMailException(MailException ex){
        return new ResponseEntity<>(Map.of(KEY_MESSAGE, EmailMessages.ERROR_SENDING_EMAIL_MESSAGE), HttpStatus.FAILED_DEPENDENCY);
    }

    @ExceptionHandler(MessagingException.class)
    private ResponseEntity<Map<String, String>> handleMessagingException(MessagingException ex){
        return new ResponseEntity<>(Map.of(KEY_MESSAGE,  EmailMessages.ERROR_SENDING_EMAIL_MESSAGE), HttpStatus.FAILED_DEPENDENCY);
    }
}
