package com.acckwu.chatapi.global.config;

import com.acckwu.chatapi.domain.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // JWT 기반 REST API는 세션을 사용하지 않기 때문에 CSRF 방어가 불필요
        http
                //CORS 활성화
                .cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)
                // /api/auth/**: 인증 없이 접근 가능, 나머지 경로: JWT 인증 필요
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/",
                                "/actuator/**",
                                "/ws/**",
                                "/ws-chat",
                                "/ws-chat/**",
                                "/api/auth/**",
                                "/api/health/**").permitAll()
                        .anyRequest().authenticated()
                )
                // JWT 필터 등록
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    // CORS 정책 정의
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 프론트 주소
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.addAllowedOrigin("http://localhost:3001");
        config.setAllowCredentials(true);

        // SockJS/STOMP에서 Authorization 헤더 사용
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
