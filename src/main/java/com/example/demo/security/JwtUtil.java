package com.example.demo.security;

import com.example.demo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final String SECRET = "secretkey123";

    // -------------------------------------------------
    // Generate token with custom claims
    // -------------------------------------------------
    public String generateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    // -------------------------------------------------
    // Generate token for User (USED IN TESTS)
    // -------------------------------------------------
    public String generateTokenForUser(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());

        return generateToken(claims, user.getEmail());
    }

    // -------------------------------------------------
    // PARSE TOKEN (IMPORTANT – FIX 2)
    // -------------------------------------------------
    // Tests expect getPayload(), NOT getBody()
    public Jwt<?, ?> parseToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parse(token);
    }

    // -------------------------------------------------
    // Extract username (email)
    // -------------------------------------------------
    public String extractUsername(String token) {
        return (String) parseToken(token).getPayload().get("sub");
    }

    // -------------------------------------------------
    // Extract role
    // -------------------------------------------------
    public String extractRole(String token) {
        return (String) parseToken(token).getPayload().get("role");
    }

    // -------------------------------------------------
    // Extract userId
    // -------------------------------------------------
    public Long extractUserId(String token) {
        Object value = parseToken(token).getPayload().get("userId");
        return Long.valueOf(value.toString());
    }

    // -------------------------------------------------
    // Validate token
    // -------------------------------------------------
    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username);
    }
}
