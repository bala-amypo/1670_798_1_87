package com.example.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints") // ADD THIS LINE
public class AuthController {
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user") // ADD THIS LINE
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // ... your code
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login user") // ADD THIS LINE
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // ... your code
    }
}