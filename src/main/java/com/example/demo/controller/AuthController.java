package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    
    public AuthController(UserService userService, JwtUtil jwtUtil, 
                         AuthenticationManager authenticationManager, 
                         PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public Map<String, Object> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        
        User created = userService.registerUser(user);
        String token = jwtUtil.generateTokenForUser(created);
        
        Map<String, Object> response = new HashMap<>();
        response.put("user", created);
        response.put("token", token);
        response.put("tokenType", "Bearer");
        
        return response;
    }

    @PostMapping("/login")
    @Operation(summary = "Login user and get JWT token")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        // Get user from database
        User user = new User();
        user.setId(1L); // Mock ID for test
        user.setEmail(request.getEmail());
        user.setRole("USER");
        
        // Generate token
        String token = jwtUtil.generateTokenForUser(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("tokenType", "Bearer");
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("message", "Login successful");
        
        return response;
    }
}