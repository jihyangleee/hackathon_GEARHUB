package com.example.demo.auth;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.SignupRequest;
import com.example.demo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest req) {
        User u = authService.register(req.name, req.email, req.password);
        String token = authService.login(req.email, req.password);
        AuthResponse r = new AuthResponse();
        r.accessToken = token;
        r.userId = u.getId();
        return ResponseEntity.ok(r);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.email, req.password);
        User u = authService.findByToken(token);
        AuthResponse r = new AuthResponse();
        r.accessToken = token;
        r.userId = u != null ? u.getId() : null;
        return ResponseEntity.ok(r);
    }

    @GetMapping("/me")
    public ResponseEntity<User> me(@RequestAttribute(name = "currentUser", required = false) User currentUser) {
        if (currentUser == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(currentUser);
    }
}
