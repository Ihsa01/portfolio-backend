package com.example.demo.services;


import com.example.demo.config.JwtUtil;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.entity.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository =userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil= jwtUtil;
    }


    public AuthResponse login(AuthRequest authRequest) {
        Optional<Users> userOpt = userRepository.findByUsername(authRequest.getUsername());
        if (userOpt.isEmpty() || !passwordEncoder.matches(authRequest.getPassword(), userOpt.get().getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(authRequest.getUsername());
        return new AuthResponse("Login successful", token);
    }

    public void register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUserPassword(UUID id, String newPassword) {
        Optional<Users> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
