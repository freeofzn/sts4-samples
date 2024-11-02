package com.example.springjwt.security;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.springjwt.mapper.RefreshTokenMapper;
import com.example.springjwt.model.RefreshTokenDTO;
import com.example.springjwt.model.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenMapper refreshTokenMapper;

    // 생성자
    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshTokenMapper refreshTokenMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    // 인증을 시도하는 메소드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {        	
            // Form 요청시 
            // String username = obtainUsername(request);
            // String password = obtainPassword(request);
            
            // JSON 요청시
            ObjectMapper objectMapper = new ObjectMapper();
            UserDTO userDTO = objectMapper.readValue(request.getInputStream(), UserDTO.class);
            String username = userDTO.getUsername();
            String password = userDTO.getPassword();	        
    
            // 스프링 시큐리티에서 username과 password를 검증하기 위해 토큰에 담아야 함
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            // 토큰을 AuthenticationManager로 전달하여 인증 처리
            return authenticationManager.authenticate(authToken); 
        } catch (Exception e) {
            throw new AuthenticationServiceException("Failed to parse user data from request", e);
        }   	        
    }

    // 로그인 성공 시 실행되는 메소드: JWT 발급 로직
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 생성 (테스트를 위하여 만료시간을 짧게 설정 = access: 10초 / refresh: 60초)
        String access = jwtUtil.createJwt("access", username, role, 10 * 1000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 60 * 1000L);

        // Refresh 토큰 DB 저장
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
        refreshTokenDTO.setUsername(username);
        refreshTokenDTO.setRefreshToken(refresh);
        refreshTokenDTO.setExpiration(jwtUtil.getExpiration(refresh));
        refreshTokenMapper.insertRefreshToken(refreshTokenDTO);
        
        log.info("로그인성공 username : {}", username);
        log.info("로그인성공 access  토큰 발급완료 : {}", access);
        log.info("로그인성공 refresh 토큰 발급완료 : {}", refresh);

        // 응답 설정
        response.addHeader("Authorization", "Bearer " + access);
        response.addHeader("Set-Cookie", createResponseCookie("refresh", refresh).toString());
        response.setStatus(HttpStatus.OK.value());
    }

    // 로그인 실패 시 실행되는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
    
    // ResponseCookie 생성 메서드
    private ResponseCookie createResponseCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .httpOnly(true)          // 자바스크립트에서 쿠키 접근 차단 (XSS 방어)
                .maxAge(60)              // 쿠키 만료 시간: 테스트를 위하여 만료시간을 짧게 설정 = 60초
                .sameSite("None")        // 크로스 사이트 요청에서도 쿠키 전송 가능
                .secure(true)            // HTTPS에서만 사용 (HTTPS 환경에서만 쿠키 전송)
                .path("/")               // 경로 설정: "/"로 설정하여 모든 경로에서 쿠키 전송
                .build();
    }
}
