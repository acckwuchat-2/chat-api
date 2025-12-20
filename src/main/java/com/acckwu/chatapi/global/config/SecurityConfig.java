package com.acckwu.chatapi.global.config;

import com.acckwu.chatapi.domain.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // JWT 기반 REST API는 세션을 사용하지 않기 때문에 CSRF 방어가 불필요
        http
                //CORS 활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                // /api/auth/**: 인증 없이 접근 가능, 나머지 경로: JWT 인증 필요
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
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
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        var config = new org.springframework.web.cors.CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("http://localhost:3000");
        config.addAllowedOriginPattern("https://3th-chat-ui.vercel.app");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
