package com.crudapp.controller;

import com.crudapp.entity.AuthRequest;
import com.crudapp.entity.UserInfo;
import com.crudapp.service.JwtService;
import com.crudapp.service.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserInfoController {

    private final UserInfoService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



    public UserInfoController(UserInfoService service, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@Valid @RequestBody UserInfo userInfo) {
        String response = service.addUser(userInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<UserInfo>> getAllUsersInfo() {
        List<UserInfo> users = service.getAllUsersInfo();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<UserInfo> getUserInfoById(@Valid @PathVariable Integer id) {
        UserInfo user = service.getUserById(id);
        return ResponseEntity.ok(user);
    }


//


    @PostMapping("/generateToken")
    public ResponseEntity<String> authenticateAndGetToken(@Valid @RequestBody AuthRequest authRequest) {
        try {
            // Try to authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authRequest.getUsername());
                return ResponseEntity.ok(token);
            } else {
                throw new UsernameNotFoundException("Invalid user request!");
            }

        } catch (BadCredentialsException ex) {
            // Handle incorrect credentials
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials");
        }
    }





    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateUserInfo(@Valid @PathVariable Integer id, @Valid @RequestBody UserInfo userInfo) {
        service.updateUser(id, userInfo);
        return ResponseEntity.ok("User updated successfully");
    }


    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUserInfo(@PathVariable Integer id) {
        service.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }



    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }



}










