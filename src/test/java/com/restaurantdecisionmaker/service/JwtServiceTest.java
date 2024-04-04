package com.restaurantdecisionmaker.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private String userName = "testUser";
    private String secret = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @BeforeEach
    public void setUp() {
        JwtService.SECRET = secret;
    }

    @Test
    public void testExtractUsername_ValidToken() {
        String token = generateToken(userName);
        String extractedUsername = jwtService.extractUsername(token);

        assertEquals(userName, extractedUsername);
    }

    @Test(expected = MalformedJwtException.class)
    public void testExtractUsername_InvalidTokenMalformed() {
        String invalidToken = "invalid_token";
        jwtService.extractUsername(invalidToken);
    }

    @Test
    public void testExtractExpiration_ValidToken() {
        String token = generateToken(userName);
        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration); // Ensure a date is extracted
    }

    @Test(expected = Exception.class)
    public void testExtractExpiration_InvalidToken() {
        String invalidToken = "invalid_token";
        jwtService.extractExpiration(invalidToken);
    }

    @Test
    public void testValidateToken_ValidToken() {
        String token = generateToken(userName);
        UserDetails mockUserDetails = new User(userName, "", Collections.emptyList());

        boolean isValid = jwtService.validateToken(token, mockUserDetails);

        assertTrue(isValid);
    }

    @Test(expected = MalformedJwtException.class)
    public void testValidateToken_InvalidTokenMalformed() {
        String invalidToken = "invalid_token";
        UserDetails mockUserDetails = new User(userName, "", Collections.emptyList());

        boolean isValid = jwtService.validateToken(invalidToken, mockUserDetails);

        assertFalse(isValid);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String userName){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims, userName);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}