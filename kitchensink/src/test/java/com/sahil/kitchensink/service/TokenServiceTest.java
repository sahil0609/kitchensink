package com.sahil.kitchensink.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "security.jwt.secretKey=dGhpcyBzZWNyZXQga2V5IHNob3VsZCBiZSBhIHZlcnkgbGFyZ2Ugc3RyaW5nIHRoYXQgaXMgbm90IGVhc3kgdG8gZ3Vlc3Mgb3RoZXJ3aXNlIHlvdSB3aWxsIGJlIGhhY2tlZA==",
        "security.jwt.expirationTime=3600000"
})
@Import(TokenService.class)
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;
    @Mock
    private UserDetails userDetails;

    @Test
    void extractUsername_ReturnsCorrectUsername() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = tokenService.generateToken(userDetails);

        String username = tokenService.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    void generateToken_ReturnsValidToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        String token = tokenService.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(tokenService.isTokenValid(token, userDetails));
    }


    @Test
    void isTokenValid_ReturnsFalse_WhenUsernameDoesNotMatch() {
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        String token = tokenService.generateToken(userDetails);
        //change the username of the mock
        when(userDetails.getUsername()).thenReturn("wronguser");
        assertFalse(tokenService.isTokenValid(token, userDetails));
    }

    @Nested
    public class TestExpiredtoken {

        @BeforeEach
        public void setup() {
            ReflectionTestUtils.setField(tokenService, "jwtExpiration", 0);
        }
        @AfterEach
        public void cleanup() {
            ReflectionTestUtils.setField(tokenService, "jwtExpiration", 360000);
        }
        @Test
        void isTokenValid_ReturnsFalse_WhenTokenExpired() {
            when(userDetails.getUsername()).thenReturn("testUser");
            when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
            String token = tokenService.generateToken(userDetails);
            //change the expiration time of the token
            assertThrows(ExpiredJwtException.class, () -> tokenService.isTokenValid(token, userDetails));

        }
    }

    @Test
    void isTokenValid_ReturnsFalse_WhentokenExpired() {
        //setting expiration time as immeditate
        //setup
        ReflectionTestUtils.setField(tokenService, "jwtExpiration", 0);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        String token = tokenService.generateToken(userDetails);
        //change the expiration time of the token
        assertThrows(ExpiredJwtException.class, () -> tokenService.isTokenValid(token, userDetails));

        //cleanup
        ReflectionTestUtils.setField(tokenService, "jwtExpiration", 360000);
    }


    @Test
    void extractClaim_ReturnsCorrectClaim() {
        String token = tokenService.generateToken(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");

        Date expiration = tokenService.extractClaim(token, Claims::getExpiration);

        assertNotNull(expiration);
    }
}