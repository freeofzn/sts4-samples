package com.example.springjwt.security;

import java.io.IOException;

import org.springframework.http.ResponseCookie;
import org.springframework.web.filter.GenericFilterBean;

import com.example.springjwt.mapper.RefreshTokenMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshTokenMapper refreshTokenMapper;

    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshTokenMapper refreshTokenMapper) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenMapper = refreshTokenMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws IOException, ServletException {

        // 1. 요청 경로가 "/logout"인지 확인
        if (!"/logout".equals(request.getRequestURI()))  {
            filterChain.doFilter(request, response);  // 로그아웃 경로가 아니면 필터 체인 실행
            return;
        }

        // 2. 요청 메서드가 POST인지 확인
        if (!"POST".equals(request.getMethod())) {
            filterChain.doFilter(request, response);  // POST가 아니면 필터 체인 실행
            return;
        }

        // 3. 쿠키에서 Refresh 토큰 추출
        String refreshToken = extractRefreshToken(request);

        // 4. Refresh 토큰이 없는 경우 처리
        if (refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 400 Bad Request 응답
            return;
        }

        // 5. Refresh 토큰이 만료되었는지 확인
        try {
            jwtUtil.isExpired(refreshToken);  // 만료 여부 확인
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // 만료된 경우 400 Bad Request 응답
            return;
        }

        // 6. Refresh 토큰인지 확인 (발급 시 페이로드에 명시)
        if (!"refresh".equals(jwtUtil.getCategory(refreshToken))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // Refresh 토큰이 아니면 400 Bad Request 응답
            return;
        }

        // 7. DB에서 Refresh 토큰이 존재하는지 확인
        if (!refreshTokenMapper.existsByRefreshToken(refreshToken)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // DB에 토큰이 없으면 400 Bad Request 응답
            return;
        }

        // 8. 로그아웃 처리 - DB에서 Refresh 토큰 삭제
        refreshTokenMapper.deleteRefreshToken(refreshToken);

        // 9. Refresh 토큰 쿠키 삭제 (만료시킴)
        ResponseCookie refreshCookie = ResponseCookie.from("refresh", null)
                .maxAge(0)              // 만료 시간 0으로 설정 (즉시 만료)
                .path("/")              // 경로 설정
                .httpOnly(true)         // HttpOnly 설정
                .build();

        // 10. 쿠키를 응답에 추가하여 삭제
        response.addHeader("Set-Cookie", refreshCookie.toString());
        
        // 11. 로그아웃 성공 응답
        response.setStatus(HttpServletResponse.SC_OK);  // 200 OK 응답
    }

    // Refresh 토큰 추출
    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}