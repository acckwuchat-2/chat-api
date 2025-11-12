package com.acckwu.chatapi.domain.auth.filter;

import com.acckwu.chatapi.domain.auth.JwtProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider tokenProvider;

    public JwtAuthenticationFilter(JwtProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        // Authorization 헤더에서 토큰 추출
        String token = resolveToken(request);

        // 토큰 검증 및 인증 설정
        if (token != null && tokenProvider.validateToken(token)) {
            var auth = tokenProvider.getAuthentication(token);
            // 현재 요청의 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 다음 필터로 요청을 넘김
        filterChain.doFilter(request, response);
    }

    // 토큰 추출 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        // 토큰이 없거나 형식이 잘못된 경우
        // 형식 예시) Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6...
        return null;
    }
}
