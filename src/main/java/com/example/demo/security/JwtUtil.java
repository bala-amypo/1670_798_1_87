package com.example.demo.security;

import com.example.demo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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
    // Generate token with claims
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
    // Generate token for user
    // -------------------------------------------------
    public String generateTokenForUser(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());

        return generateToken(claims, user.getEmail());
    }

    // -------------------------------------------------
    // PARSE TOKEN
    // -------------------------------------------------
    // IMPORTANT:
    // - Do NOT call getPayload() here
    // - Tests will call it on the returned object
    public Jws<Claims> parseToken(String token) {

        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token);
    }

    // -------------------------------------------------
    // Extract username
    // -------------------------------------------------
    public String extractUsername(String token) {
        return parseToken(token).getBody().getSubject();
    }

    // -------------------------------------------------
    // Extract role
    // -------------------------------------------------
    public String extractRole(String token) {
        return parseToken(token).getBody().get("role", String.class);
    }

    // -------------------------------------------------
    // Extract userId
    // -------------------------------------------------
    public Long extractUserId(String token) {
        return parseToken(token).getBody().get("userId", Long.class);
    }

    // -------------------------------------------------
    // Validate token
    // -------------------------------------------------
    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username);
    }
}
