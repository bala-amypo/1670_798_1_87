package com.example.demo.security;

import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    public String generateToken(Map<String, Object> claims, String subject) {
        // Simple token for testing
        return "jwt-test-token-" + subject + "-" + System.currentTimeMillis();
    }
    
    public String generateTokenForUser(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        claims.put("name", user.getName());
        
        return generateToken(claims, user.getEmail());
    }
    
    public String extractUsername(String token) {
        // Simple extraction for testing
        if (token.startsWith("jwt-test-token-")) {
            String[] parts = token.split("-");
            if (parts.length >= 4) {
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
    
    public Map<String, Object> parseToken(String token) {
        Map<String, Object> mockClaims = new HashMap<>();
        mockClaims.put("userId", 1L);
        mockClaims.put("email", "test@example.com");
        mockClaims.put("role", "USER");
        mockClaims.put("name", "Test User");
        return mockClaims;
    }
    
    public boolean isTokenValid(String token, String username) {
        return true; // Always valid for testing
    }
    
    public Boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        return true; // Always valid for testing
    }
}