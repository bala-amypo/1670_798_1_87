// package com.example.demo.controller;

// import com.example.demo.dto.LoginRequest;
// import com.example.demo.dto.RegisterRequest;
// import com.example.demo.entity.User;
// import com.example.demo.security.JwtUtil;
// import com.example.demo.service.UserService;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.web.bind.annotation.*;

// import java.util.Map;

// @RestController
// @RequestMapping("/api/auth")
// public class AuthController {
//     private final UserService userService;
//     private final JwtUtil jwtUtil;
//     private final AuthenticationManager authenticationManager;

//     public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
//         this.userService = userService;
//         this.jwtUtil = jwtUtil;
//         this.authenticationManager = authenticationManager;
//     }

//     @PostMapping("/register")
//     public Map<String, Object> register(@RequestBody RegisterRequest request) {
//         User user = new User(null, request.getName(), request.getEmail(), request.getPassword(), "USER", null);
//         User created = userService.registerUser(user);
//         String token = jwtUtil.generateTokenForUser(created);
//         return Map.of("user", created, "token", token);
//     }

//     @PostMapping("/login")
//     public Map<String, String> login(@RequestBody LoginRequest request) {
//         authenticationManager.authenticate(
//             new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//         );
//         String token = jwtUtil.generateToken(Map.of(), request.getEmail());
//         return Map.of("token", token);
//     }
// }


package com.example.demo.controller;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 🔑
        user.setRole(Role.USER); // 🔑

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
