package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.Users;
import com.example.demo.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")

@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {

        String token = authService.login(authRequest).getToken();



        // Return response entity with the login success message
        return ResponseEntity.ok(new AuthResponse("Login successful", token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        authService.register(authRequest.getUsername(), authRequest.getPassword());
        return ResponseEntity.ok("User registered successfully");
    }
    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{id}/update-password")
    public ResponseEntity<String> updatePassword(@PathVariable UUID id, @RequestBody String newPassword) {
        authService.updateUserPassword(id, newPassword);
        return ResponseEntity.ok("Password updated successfully");
    }

}
