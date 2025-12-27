package com.example.demo.dto;

public class JwtResponse {
    private String token;
    private String type;
    private Long userId;
    private String email;
    private String role;
    
    public JwtResponse() {
        this.type = "Bearer";
    }
    
    public JwtResponse(String token, String type, Long userId, String email, String role) {
        this.token = token;
        this.type = type;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }
    
    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}