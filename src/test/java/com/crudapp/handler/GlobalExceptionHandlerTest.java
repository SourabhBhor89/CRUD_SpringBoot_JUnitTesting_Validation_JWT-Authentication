package com.crudapp.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setup() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleValidationExceptions() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        FieldError fieldError = new FieldError("UserInfo", "name", "Name is required");
        List<ObjectError> errors = new ArrayList<>();
        errors.add(fieldError);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(errors);

        // Act
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("name", "Name is required");
    }

    @Test
    public void testHandleBadCredentialsException() {
        // Arrange
        BadCredentialsException ex = new BadCredentialsException("Invalid credentials");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleBadCredentialsException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid credentials");
    }

    @Test
    public void testHandleUsernameNotFoundException() {
        // Arrange
        UsernameNotFoundException ex = new UsernameNotFoundException("User not found");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleUsernameNotFoundException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    public void testHandleResponseStatusException() {
        // Arrange
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleResponseStatusException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Resource not found");
    }

    @Test
    public void testHandleGeneralException() {
        // Arrange
        Exception ex = new Exception("An unexpected error");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleGeneralException(ex);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("An error occurred: An unexpected error");
    }
}
