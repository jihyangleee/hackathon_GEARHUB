package com.example.demo.auth;

import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();
    private final Map<String, String> tokenToEmail = new ConcurrentHashMap<>();

    public User register(String name, String email, String password) {
        if (usersByEmail.containsKey(email.toLowerCase())) {
            throw new IllegalArgumentException("User already exists");
        }
        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setName(name);
        u.setEmail(email.toLowerCase());
        u.setPassword(password); // demo: plaintext (replace with hashing for prod)
        usersByEmail.put(u.getEmail(), u);
        return u;
    }

    public String login(String email, String password) {
        User u = usersByEmail.get(email.toLowerCase());
        if (u == null) throw new IllegalArgumentException("Invalid credentials");
        if (!u.getPassword().equals(password)) throw new IllegalArgumentException("Invalid credentials");
        String token = UUID.randomUUID().toString();
        tokenToEmail.put(token, u.getEmail());
        return token;
    }

    public User findByToken(String token) {
        if (token == null) return null;
        String email = tokenToEmail.get(token);
        if (email == null) return null;
        return usersByEmail.get(email);
    }
}
