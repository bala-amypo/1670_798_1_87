package com.example.demo.security;

import com.example.demo.entity.User;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    
    private final String secret = "test-secret-key-for-jwt-token-generation-in-tests";
    
    public String generateToken(Map<String, Object> claims, String subject) {
        // Simple token generation for tests
        return "test-jwt-token-" + subject + "-" + System.currentTimeMillis();
    }
    
    public String generateTokenForUser(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        
        return generateToken(claims, user.getEmail());
    }
    
    public String extractUsername(String token) {
        // Simple extraction for tests
        if (token.startsWith("test-jwt-token-")) {
            String[] parts = token.split("-");
            if (parts.length > 3) {
                return parts[3];
            }
        }
        return "test@example.com";
    }
    
    public Long extractUserId(String token) {
        return 1L; // Return test user ID
    }
    
    public String extractRole(String token) {
        return "USER"; // Return test role
    }
    
    public Object parseToken(String token) {
        // Return a simple object for tests
        Map<String, Object> mockClaims = new HashMap<>();
        mockClaims.put("userId", 1L);
        mockClaims.put("email", "test@example.com");
        mockClaims.put("role", "USER");
        return mockClaims;
    }
    
    public boolean isTokenValid(String token, String username) {
        // Always return true for tests
        return true;
    }
}