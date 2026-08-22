package com.tunahan.starter;

import com.tunahan.starter.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {
    private JwtUtil jwtUtil;

    private final String SECRET = "v3ryS3cr3tKeyTh4tIsAtL34st32Ch4rsL0ng!";
    private final Long EXPIRATION = 3600000L;


    @BeforeEach
    void setUp(){
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", EXPIRATION);
    }

    @Test
    @DisplayName("generateToken : Geçerli parametrelerle token üretilmeli")
    void generateToken_ShouldCreateToken(){
        String token = jwtUtil.generateToken("tunahan","ROLE_ADMIN");
        assertNotNull(token);
        assertFalse(token.trim().isEmpty());
    }

    @Test
    @DisplayName("extractUsername: Tokendan doğru username bilgisi okunmalı")
    void extractUsername_ShouldReturnCorrectUsername(){
        String expectedUsername = "tunahan";
        String token = jwtUtil.generateToken(expectedUsername,"ROLE_USER");
        String actualUsername = jwtUtil.extractUsername(token);
        assertEquals(expectedUsername,actualUsername);
    }

    @Test
    @DisplayName("extractRole: Tokendan doğru rol bilgisi okunmalı")
    void extractRole_ShouldReturnCorrectRole(){
        String expectedRole = "ROLE_ADMIN";
        String token = jwtUtil.generateToken("tunahan",expectedRole);
        String actualRole = jwtUtil.extractRole(token);
        assertEquals(expectedRole,actualRole);
    }

    @Test
    @DisplayName("extractExpiration: Tokendaki son kullanma tarihi gelecekte bir zaman mı")
    void extractExpiration_ShouldReturnFutureTime(){
        long now = System.currentTimeMillis();
        String token = jwtUtil.generateToken("tunahan","ROLE_USER");
        Long expirationTime = jwtUtil.extractExpiration(token);
        assertNotNull(expirationTime);
        assertTrue(expirationTime > now);

    }


    @Test
    @DisplayName("isTokenValid: Doğru imzalanmış token için true dönmeli")
    void isTokenValid_WithValidToken_ShouldReturnTrue(){
        String token = jwtUtil.generateToken("tunahan","ROLE_USER");
        boolean isValid = jwtUtil.isTokenValid(token);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("isTokenValid: Bozuk yapıdaki token için false dönmeli")
    void isTokenValid_WithMalformedToken_ShouldReturnFalse(){
        String malformedToken = "invalid.jwt.token";
        boolean isValid = jwtUtil.isTokenValid(malformedToken);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("isTokenValid: Süresi dolmuş token için false dönmeli")
    void isTokenValid_WithExpiredToken_ShouldReturnFalse(){
        ReflectionTestUtils.setField(jwtUtil,"expiration",-5000L);
        String expiredToken = jwtUtil.generateToken("tunahan","ROLE_USER");
        boolean isValid = jwtUtil.isTokenValid(expiredToken);
        assertFalse(isValid);
    }
}
