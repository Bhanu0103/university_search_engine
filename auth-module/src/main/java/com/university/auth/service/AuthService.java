package com.university.auth.service;

import com.university.auth.dto.AuthRecord;
import com.university.auth.dto.RegisterRecord;
import com.university.common.entity.UserEntity;
import com.university.common.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public String authenticate(AuthRecord record) {
        UserEntity user = userRepository.findByEmail(record.email())
                .orElseThrow(() -> new RuntimeException("Authentication failed: User not found with email " + record.email()));

        if (passwordEncoder.matches(record.password(), user.getPassword())) {
            return jwtService.generateToken(user.getEmail());
        }
        
        throw new RuntimeException("Authentication failed: Invalid password");
    }

    public void register(RegisterRecord record) {
        if (userRepository.findByEmail(record.email()).isPresent()) {
            throw new RuntimeException("Registration failed: Email already exists");
        }
        if (userRepository.findByUsername(record.username()).isPresent()) {
            throw new RuntimeException("Registration failed: Username already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(record.username());
        user.setPassword(passwordEncoder.encode(record.password()));
        user.setEmail(record.email());
        user.setRole(record.role());

        userRepository.save(user);
    }

    public boolean validate(String token) {
        return jwtService.validateToken(token);
    }

    public UserEntity getUserFromToken(String token) {
        String email = jwtService.getUsernameFromToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found for token"));
    }
}
