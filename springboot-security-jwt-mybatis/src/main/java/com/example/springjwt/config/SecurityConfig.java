package com.example.springjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.example.springjwt.mapper.RefreshTokenMapper;
import com.example.springjwt.security.CustomLogoutFilter;
import com.example.springjwt.security.JWTFilter;
import com.example.springjwt.security.JWTUtil;
import com.example.springjwt.security.LoginFilter;
import com.example.springjwt.security.SecurityConstants;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshTokenMapper refreshTokenMapper;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RefreshTokenMapper refreshTokenMapper) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1. CSRF 보호 비활성화 (JWT 사용 시 필요 없음)
        http.csrf((auth) -> auth.disable());

        // 2. Form 로그인 비활성화
        http.formLogin((auth) -> auth.disable());

        // 3. HTTP 기본 인증 비활성화
        http.httpBasic((auth) -> auth.disable());

        // 4. 경로별 권한 부여 설정
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/html/**", "/css/**", "/js/**", "/favicon.ico").permitAll()    // 정적 리소스 허용
                .requestMatchers(SecurityConstants.PERMIT_ALL_URLS).permitAll()                  // 인증 없이 접근 가능 경로
                .requestMatchers("/admin").hasRole("ADMIN")                                      // 관리자 전용 경로
                .requestMatchers("/sale").hasAnyRole("SALE", "ADMIN")                            // SALE 및 ADMIN 접근 가능
                .requestMatchers("/acct").hasAnyRole("ACCT", "ADMIN")                            // ACCT 및 ADMIN 접근 가능
                .anyRequest().authenticated());                                                  // 그 외 모든 요청 인증 필요

        // 5. JWT 필터 설정: JWTFilter를 LoginFilter 이전에 추가하여 JWT 인증 처리
        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        // 6. 로그인 필터 설정: LoginFilter를 UsernamePasswordAuthenticationFilter 위치에 추가
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenMapper), 
                         UsernamePasswordAuthenticationFilter.class);

        // 7. 로그아웃 필터 설정: CustomLogoutFilter를 LogoutFilter 이전에 추가
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenMapper), LogoutFilter.class);

        // 8. 세션 관리 설정: 세션을 사용하지 않도록 STATELESS로 설정
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
