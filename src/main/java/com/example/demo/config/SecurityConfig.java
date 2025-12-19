package com.example.demo.config;

// ... imports ...

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // ... existing code ...
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // Public endpoints (no authentication required)
                .antMatchers("/").permitAll()
                .antMatchers("/health").permitAll()
                .antMatchers("/auth/**").permitAll()
                
                // CRITICAL: Add these Swagger paths
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/api-docs/**").permitAll()
                
                // All other API endpoints require authentication
                .antMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    // ... rest of your code ...
}