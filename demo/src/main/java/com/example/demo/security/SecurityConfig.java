package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // Use IF_REQUIRED so UI flows that rely on HttpSession (signup/login redirect flow) can create a session.
                // JWT is still supported via the JwtAuthenticationFilter, but session usage is allowed for the Thymeleaf UI.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeHttpRequests()
                // allow public access to home, auth pages (GET), static resources, category and article pages
                .requestMatchers("/", "/login", "/signup", "/mypage", "/mypage/**", "/css/**", "/js/**", "/images/**", "/api/auth/**", "/category/**", "/article/**", "/search**", "/product/**").permitAll()
                // explicitly allow form posts for authentication endpoints (POST)
                .requestMatchers(HttpMethod.POST, "/login", "/signup").permitAll()
                // explicitly allow GET to mypage as well
                .requestMatchers(HttpMethod.GET, "/mypage", "/mypage/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
