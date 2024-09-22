package com.sahil.kitchensink.filter;

import com.sahil.kitchensink.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class JWTTokenFilterTest {

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;

    @Mock
    private TokenService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    void cleanUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_ResolvesException_WhenExceptionThrown() throws ServletException, IOException {
        when(request.getHeader(JwtTokenFilter.AUTHORIZATION_HEADER)).thenReturn("Bearer invalidToken");
        doThrow(new RuntimeException("Test Exception")).when(jwtService).extractUsername(anyString());

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(handlerExceptionResolver, times(1)).resolveException(eq(request), eq(response), isNull(), any(RuntimeException.class));
    }

    @Test
    void doFilterInternal_SetsAuthentication_WhenTokenIsInValid() throws ServletException, IOException {
        String token = "invalidToken";
        String username = "testUser";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader(JwtTokenFilter.AUTHORIZATION_HEADER)).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_SetsAuthentication_WhenTokenIsInValid_tokenException() throws ServletException, IOException {
        String token = "invalidToken";
        String username = "testUser";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader(JwtTokenFilter.AUTHORIZATION_HEADER)).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenThrow(ExpiredJwtException.class);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(handlerExceptionResolver, times(1)).resolveException(eq(request), eq(response), isNull(), any(JwtException.class));
    }

    @Test
    void doFilterInternal_ContinuesFilterChain_WhenNoAuthHeader() throws ServletException, IOException {
        when(request.getHeader(JwtTokenFilter.AUTHORIZATION_HEADER)).thenReturn(null);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ContinuesFilterChain_WhenAuthHeaderDoesNotStartWithBearer() throws ServletException, IOException {
        when(request.getHeader(JwtTokenFilter.AUTHORIZATION_HEADER)).thenReturn("InvalidHeader");

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_SetsAuthentication_WhenTokenIsValid() throws ServletException, IOException {
        String token = "validToken";
        String username = "testUser";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader(JwtTokenFilter.AUTHORIZATION_HEADER)).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }



}