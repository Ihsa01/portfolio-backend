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

    @Value("${jwt.secret-key}")
    private String secretKey;

    private Key secretKeyDecoded;

    private static final long EXPIRATION_TIME = 86400000;  // 1 day

    @PostConstruct
    public void init() {
        if (secretKey.length() < 32) {
            this.secretKeyDecoded = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            this.secretKeyDecoded = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        }
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKeyDecoded)
                .compact();
    }



}
