package com.university.auth.service;

import com.university.auth.dto.AuthRecord;
import com.university.auth.dto.RegisterRecord;
import com.university.common.entity.UserEntity;
import com.university.common.enums.Role;
import com.university.common.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final JwtService jwtService = mock(JwtService.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthService service = new AuthService(userRepository, jwtService, passwordEncoder);

    @Test
    void authenticateReturnsGeneratedTokenForMatchingPassword() {
        UserEntity user = new UserEntity();
        user.setEmail("faculty@example.com");
        user.setPassword("encoded");
        when(userRepository.findByEmail("faculty@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "encoded")).thenReturn(true);
        when(jwtService.generateToken("faculty@example.com")).thenReturn("jwt-token");

        assertThat(service.authenticate(new AuthRecord("faculty@example.com", "secret"))).isEqualTo("jwt-token");
    }

    @Test
    void registerEncodesPasswordAndSavesUser() {
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("student")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("encoded");

        service.register(new RegisterRecord("student", "secret", "student@example.com", Role.STUDENT));

        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void registerRejectsDuplicateEmail() {
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(new UserEntity()));

        assertThatThrownBy(() -> service.register(new RegisterRecord("student", "secret", "student@example.com", Role.STUDENT)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already exists");
    }
}
