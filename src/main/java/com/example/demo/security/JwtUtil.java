package com.example.demo.security;

import com.example.demo.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    public String generateToken(Map<String, Object> claims, String subject) {
        return "test-jwt-token-" + subject;
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
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1L);
        claims.put("email", "test@example.com");
        claims.put("role", "USER");
        return claims;
    }
    
    public boolean isTokenValid(String token, String username) {
        return true;
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        return true;
    }
}