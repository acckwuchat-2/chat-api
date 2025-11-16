package com.acckwu.chatapi.domain.auth.service;

import com.acckwu.chatapi.domain.auth.JwtProvider;
import com.acckwu.chatapi.domain.user.dto.CreateUserDto;
import com.acckwu.chatapi.domain.user.entity.User;
import com.acckwu.chatapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String register(CreateUserDto dto) {
        // username 중복 방지
        userRepository.findByUsername(dto.getUsername())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Username already exists");
                });

        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        userRepository.save(user);

        return jwtProvider.generateToken(user.getUserId());
    }

    public String login(CreateUserDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("Invalid password");

        return jwtProvider.generateToken(user.getUserId());
    }
}
