package com.university.auth.service;

import com.university.auth.dto.AuthRecord;
import com.university.auth.dto.RegisterRecord;
import com.university.common.exception.AuthenticationFailedException;
import com.university.common.exception.DuplicateResourceException;
import com.university.common.exception.ResourceNotFoundException;
import com.university.common.entity.UserEntity;
import com.university.common.repository.UserRepository;
import com.university.common.validation.ValidationSupport;
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
        ValidationSupport.validate(record);
        UserEntity user = userRepository.findByEmail(record.email())
                .orElseThrow(() -> new AuthenticationFailedException("Authentication failed: user not found with email " + record.email()));

        if (passwordEncoder.matches(record.password(), user.getPassword())) {
            return jwtService.generateToken(user.getEmail());
        }
        
        throw new AuthenticationFailedException("Authentication failed: invalid password");
    }

    public void register(RegisterRecord record) {
        ValidationSupport.validate(record);
        if (userRepository.findByEmail(record.email()).isPresent()) {
            throw new DuplicateResourceException("Registration failed: email already exists");
        }
        if (userRepository.findByUsername(record.username()).isPresent()) {
            throw new DuplicateResourceException("Registration failed: username already exists");
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found for token"));
    }
}
