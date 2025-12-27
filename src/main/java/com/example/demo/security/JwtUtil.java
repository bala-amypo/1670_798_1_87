package com.example.demo.security;

import com.example.demo.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    
    public String generateToken(Map<String, Object> claims, String subject) {
        return "test-token-" + subject + "-" + System.currentTimeMillis();
    }
    
    public String generateTokenForUser(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        
        return generateToken(claims, user.getEmail());
    }
    
    public String extractUsername(String token) {
        return "test@example.com";
    }
    
    public Long extractUserId(String token) {
        return 1L;
    }
    
    public String extractRole(String token) {
        return "USER";
    }
    
    public Map<String, Object> parseToken(String token) {
        Map<String, Object> mockClaims = new HashMap<>();
        mockClaims.put("userId", 1L);
        mockClaims.put("email", "test@example.com");
        mockClaims.put("role", "USER");
        return mockClaims;
    }
    
    public boolean isTokenValid(String token, String username) {
        return true;
    }
    
    // Add this missing method
    public Boolean validateToken(String token, UserDetails userDetails) {
        return true;
    }
}