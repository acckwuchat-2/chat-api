package com.acckwu.chatapi.domain.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtProvider {
    // 임시 키(수정 필요)
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "this_is_a_very_long_and_secure_secret_key".getBytes()
    );

    // JWT 생성 메서드
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date()) // 발급 시간 기록
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 만료 시간(24시간)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact(); // 실제 JWT 문자열 반환
    }

    // JWT에서 인증 정보를 추출하는 메서드
    public Authentication getAuthentication(String token) {
        String userId = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // userId

        CustomUserDetails userDetails = new CustomUserDetails(userId);

        return new UsernamePasswordAuthenticationToken(userDetails, "", List.of());
    }

    // JWT 검증 메서드
    public boolean validateToken(String token) {
        try {
            // JWT의 서명과 유효기간 검사
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
