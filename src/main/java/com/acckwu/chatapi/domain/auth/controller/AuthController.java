package com.acckwu.chatapi.domain.auth.controller;

import com.acckwu.chatapi.domain.user.dto.CreateUserDto;
import com.acckwu.chatapi.domain.auth.dto.AccessTokenDto;
import com.acckwu.chatapi.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 회원가입 API
    @PostMapping("/register")
    public ResponseEntity<AccessTokenDto> register(@RequestBody CreateUserDto dto) {
        String token = authService.register(dto);

        return ResponseEntity.ok(new AccessTokenDto(token));
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<AccessTokenDto> login(@RequestBody CreateUserDto dto) {
        String token = authService.login(dto);

        return ResponseEntity.ok(new AccessTokenDto(token));
    }
}
