package com.example.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key;
    private final long validityMs = 1000L * 60 * 60 * 24; // 24h

    public JwtUtil(@Value("${app.jwt.secret:}") String secret) {
        // If a sufficiently long secret is provided, derive HMAC key from it. Otherwise generate a strong random key.
        if (secret != null && secret.trim().length() >= 32) {
            this.key = Keys.hmacShaKeyFor(secret.trim().getBytes(StandardCharsets.UTF_8));
        } else {
            // Warning: generated key will change across restarts. Set app.jwt.secret in properties or env for persistent tokens.
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
    }

    public String generateToken(String userId) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validityMs))
                .signWith(key)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
