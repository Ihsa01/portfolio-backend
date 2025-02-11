package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.Users;
import com.example.demo.services.AuthService;
import com.example.demo.utils.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public ResponseEntity<ApiResponse<String>> register(@RequestBody AuthRequest authRequest) {
        try {
            authService.register(authRequest.getUsername(), authRequest.getPassword());
            ApiResponse<String> response = new ApiResponse<>(200, "User registered successfully", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(400, e.getMessage(), null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = authService.getAllUsers()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername()))
                .collect(Collectors.toList());

        ApiResponse<List<UserResponse>> response = new ApiResponse<>(200, "Users retrieved successfully", users);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}/update-password")
    public ResponseEntity<ApiResponse<String>> updatePassword(@PathVariable UUID id, @RequestBody Map<String, String> request) {
        try {
            String newPassword = request.get("newPassword");
            authService.updateUserPassword(id, newPassword);
            ApiResponse<String> response = new ApiResponse<>(200, "Password updated successfully", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(400, e.getMessage(), null);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


}
