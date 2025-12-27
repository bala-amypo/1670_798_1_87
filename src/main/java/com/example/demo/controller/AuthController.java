package com.example.demo.controller;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setFullName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole("USER");
        
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        // For Swagger demo, create a mock response
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail(request.getEmail());
        mockUser.setRole("USER");
        
        String token = jwtUtil.generateTokenForUser(mockUser);
        
        JwtResponse response = new JwtResponse();
        response.setToken(token);
        response.setType("Bearer");
        response.setUserId(mockUser.getId());
        response.setEmail(mockUser.getEmail());
        response.setRole(mockUser.getRole());
        
        return ResponseEntity.ok(response);
    }
}