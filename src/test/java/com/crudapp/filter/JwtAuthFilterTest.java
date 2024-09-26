package com.crudapp.filter;

import com.crudapp.service.JwtService;
import com.crudapp.service.UserInfoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class JwtAuthFilterTest {

    private JwtAuthFilter jwtAuthFilter;
    private JwtService jwtService;
    private UserInfoService userInfoService;

    @BeforeEach
    public void setUp() {
        jwtService = mock(JwtService.class);
        userInfoService = mock(UserInfoService.class);
        jwtAuthFilter = new JwtAuthFilter(jwtService, userInfoService);
        SecurityContextHolder.clearContext(); // Clear context before each test
    }

    @Test
    public void testDoFilterInternal_WhenAuthHeaderIsMissing() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        // Assert that SecurityContextHolder is still empty after the filter is called
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void testDoFilterInternal_WhenTokenIsInvalid() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String token = "invalidToken";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn("user");
        when(userInfoService.loadUserByUsername("user")).thenReturn(null);
        when(jwtService.validateToken(eq(token), any(UserDetails.class))).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        // Assert that SecurityContextHolder is still empty since token is invalid
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void testDoFilterInternal_WhenTokenIsValid() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        String token = "validToken";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn("user");
        when(userInfoService.loadUserByUsername("user")).thenReturn(userDetails);
        when(jwtService.validateToken(eq(token), eq(userDetails))).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        // Check if the authentication was set correctly
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(userDetails);
    }
}
