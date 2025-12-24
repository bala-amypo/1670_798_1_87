package com.example.demo.config;

import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    /* ================= CORE BEANS ================= */

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* ================= USER DETAILS ================= */

    @Bean
    public CustomUserDetailsService customUserDetailsService(
            UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    /* ================= AUTH PROVIDER ================= */

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /* ================= AUTH MANAGER ================= */

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /* ================= SECURITY FILTER ================= */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtUtil jwtUtil) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // ✅ Swagger + Auth must be PUBLIC
                .requestMatchers(
                        "/auth/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html"
                ).permitAll()

                // ✅ API protected
                .requestMatchers("/api/**").authenticated()

                // everything else allowed
                .anyRequest().permitAll()
            )

            // ✅ Register authentication provider
            .authenticationProvider(authenticationProvider(
                    customUserDetailsService(null),
                    passwordEncoder()
            ))

            // ✅ JWT filter
            .addFilterBefore(
                    new JwtAuthenticationFilter(jwtUtil),
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
