package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping
public class AuthController {

    private final UserRepository userRepository;
    private final org.springframework.security.authentication.AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          org.springframework.security.authentication.AuthenticationManager authManager,
                          JwtUtil jwtUtil,
                          org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, Model model,
                        jakarta.servlet.http.HttpServletResponse response) {
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
        User user = userOpt.get();

        // If stored password is not a BCrypt hash (legacy plain-text in dump), allow login by direct match
        // and upgrade to BCrypt automatically.
        String stored = user.getPassword();
        boolean storedLooksHashed = stored != null && (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$"));

        try {
            if (!storedLooksHashed) {
                // legacy plain-text password in DB
                if (!password.equals(stored)) {
                    model.addAttribute("error", "Invalid username or password");
                    return "login";
                }
                // upgrade stored password to BCrypt
                user.setPassword(this.passwordEncoder.encode(password));
                userRepository.save(user);
            }

            var token = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(username, password);
            var auth = this.authManager.authenticate(token);
            // successful
            String jwt = this.jwtUtil.generateToken(username);
            jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("JWT_TOKEN", jwt);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 1 day
            session.setAttribute("user", user);
            response.addCookie(cookie);
            return "redirect:/";
        } catch (Exception ex) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
    model.addAttribute("userForm", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        if (userRepository.findByUsername(userForm.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already exists");
            return "signup";
        }
        // Ensure createdAt and email set minimally for new user
        if (userForm.getCreatedAt() == null) userForm.setCreatedAt(java.time.LocalDateTime.now());
        if (userForm.getEmail() == null) userForm.setEmail(userForm.getUsername() + "@example.com");
        // encode password
        userForm.setPassword(this.passwordEncoder.encode(userForm.getPassword()));
        userRepository.save(userForm);
        session.setAttribute("user", userForm);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, jakarta.servlet.http.HttpServletResponse response) {
        session.invalidate();
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("JWT_TOKEN", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
