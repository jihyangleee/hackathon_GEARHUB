package com.example.demo.auth;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(String name, String email, String password) {
        if (userRepository.findByEmail(email.toLowerCase()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        User u = new User();
        u.setName(name);
        u.setEmail(email.toLowerCase());
        u.setPasswordHash(passwordEncoder.encode(password));
        return userRepository.save(u);
    }

    public String login(String email, String password) {
        Optional<User> ou = userRepository.findByEmail(email.toLowerCase());
        if (ou.isEmpty()) throw new IllegalArgumentException("Invalid credentials");
        User u = ou.get();
        if (!passwordEncoder.matches(password, u.getPasswordHash())) throw new IllegalArgumentException("Invalid credentials");
        return jwtUtil.generateToken(u.getId());
    }

    public User findByToken(String token) {
        if (token == null) return null;
        try{
            String userId = jwtUtil.validateAndGetUserId(token);
            return userRepository.findById(userId).orElse(null);
        }catch(Exception e){
            return null;
        }
    }
}
