package com.example.demo.security;

import com.example.demo.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET =
            "THIS_IS_A_VERY_SECURE_SECRET_KEY_FOR_JWT_SIGNING_123456";

    private static final long EXPIRATION = 1000 * 60 * 60 * 10;

    // ✅ MUST be SecretKey for jjwt 0.12.x
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /* ================= TOKEN CREATION ================= */

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public String generateTokenForUser(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        return generateToken(claims, user.getEmail());
    }

    /* ================= TOKEN READING ================= */

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Object id = claims.get("userId");
        return id == null ? null : Long.valueOf(id.toString());
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username);
    }

    /* ================= CORE PARSER ================= */

    public Jwt<?, ?> parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)   // ✅ Correct type
                .build()
                .parse(token);
    }

    private Claims extractAllClaims(String token) {
        return (Claims) parseToken(token).getPayload();
    }
}
