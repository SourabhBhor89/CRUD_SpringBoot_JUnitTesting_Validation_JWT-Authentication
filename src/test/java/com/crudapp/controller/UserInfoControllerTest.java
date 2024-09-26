package com.crudapp.controller;
import com.crudapp.controller.UserInfoController;
import com.crudapp.entity.AuthRequest;
import com.crudapp.entity.UserInfo;
import com.crudapp.service.JwtService;
import com.crudapp.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserInfoControllerTest {

    @InjectMocks
    private UserInfoController userInfoController;

    @Mock
    private UserInfoService userInfoService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    private UserInfo testUser;
    private AuthRequest authRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserInfo();
        testUser.setName("testUser");
        testUser.setPassword("testPassword");
        testUser.setRoles("USER");

        authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPassword");
    }

    @Test
    public void testAuthenticateAndGetToken_Success() {
        // Mock the behavior of the AuthenticationManager and JwtService
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true); // Ensure it is marked as authenticated
        when(jwtService.generateToken(authRequest.getUsername())).thenReturn("token");

        // Call the method under test
        ResponseEntity<String> response = userInfoController.authenticateAndGetToken(authRequest);

        // Check the response status and body
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testAuthenticateAndGetToken_BadCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<String> response = userInfoController.authenticateAndGetToken(authRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid credentials");
    }

    // Additional tests...
}
