package com.example.springjwt.controller;

import com.example.springjwt.service.RefreshTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@ResponseBody
public class RefreshTokenController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 1. Request에서 refresh token 추출
        String refreshToken = extractRefreshToken(request);

        // 2. 서비스 호출하여 새 토큰 발급
        Map<String, String> tokens = refreshTokenService.reissueTokens(refreshToken);
        if (tokens == null) {
            return new ResponseEntity<>("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
        }

        // 3. Access Token을 헤더에 추가
        response.addHeader("Authorization", "Bearer " + tokens.get("accessToken"));

        // 4. Refresh Token을 쿠키에 추가
        response.addHeader("Set-Cookie", createResponseCookie("refresh", tokens.get("refreshToken")).toString());

        log.info("refresh 토큰 재발급완료 : {}", tokens.get("refreshToken"));
        
        // 5. 성공 응답 반환
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Refresh Token 추출
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

    // ResponseCookie 생성 메서드
    private ResponseCookie createResponseCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .httpOnly(true)          // 자바스크립트 접근 차단
                .maxAge(10 * 60)         // 쿠키 만료 시간: 10분 (600초)
                .sameSite("None")        // SameSite 설정
                .secure(true)            // HTTPS에서만 사용
                .path("/")               // 경로 설정
                .build();
    }
}
