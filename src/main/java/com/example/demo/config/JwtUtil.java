package com.example.demo.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")  // Inject secret key from application.properties
    private String secretKey;

    private Key secretKeyDecoded;  // The decoded key

    private static final long EXPIRATION_TIME = 86400000;  // 1 day

    @PostConstruct
    public void init() {
        // Use a secure way to generate a key if secretKey is not long enough
        if (secretKey.length() < 32) {
            // This method guarantees the key will be large enough for HS256
            this.secretKeyDecoded = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            // If the provided key is sufficient, decode it
            this.secretKeyDecoded = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        }
    }

    // Method to generate JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKeyDecoded)
                .compact();
    }

    // Method to validate the token (optional - depending on your use case)
    public boolean validateToken(String token, String username) {
        String tokenUsername = Jwts.parserBuilder()
                .setSigningKey(secretKeyDecoded)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return (username.equals(tokenUsername));
    }

    // Method to extract username from token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKeyDecoded)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
