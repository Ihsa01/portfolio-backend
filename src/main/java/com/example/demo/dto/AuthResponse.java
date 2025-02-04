package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


public class AuthResponse {
    private String message;
    private String token;

    public AuthResponse() {}

    // Constructor that accepts message and token
    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}
