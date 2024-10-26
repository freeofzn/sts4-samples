package com.example.springjwt.service;

import com.example.springjwt.mapper.RefreshTokenMapper;
import com.example.springjwt.model.RefreshTokenDTO;
import com.example.springjwt.security.JWTUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenMapper refreshTokenMapper;

    @Autowired
    private JWTUtil jwtUtil;

    // Refresh 토큰을 처리하고, 새 AccessToken과 RefreshToken을 반환
    public Map<String, String> reissueTokens(String refreshToken) {
        // Refresh Token이 유효하지 않으면 null 반환
        if (refreshToken == null || jwtUtil.isExpired(refreshToken) || !isRefreshTokenValid(refreshToken)) {
            return null;
        } 
        
        // 1. DB에서 refresh token 확인
        if (!existsByRefreshToken(refreshToken)) {
            return null;
        }

        // 2. Access Token 및 Refresh Token 재발급
        String newAccessToken = createNewAccessToken(refreshToken);
        String newRefreshToken = createNewRefreshToken(refreshToken);

        // 3. DB에서 기존 Refresh Token 삭제 및 새로 저장
        updateRefreshToken(refreshToken, newRefreshToken);

        // 4. 토큰들을 Map에 담아 반환
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);

        return tokens;
    }

    // Refresh Token의 카테고리 확인
    private boolean isRefreshTokenValid(String refreshToken) {
        String category = jwtUtil.getCategory(refreshToken);
        return "refresh".equals(category);
    }

    // DB에서 refresh token이 존재하는지 확인
    public boolean existsByRefreshToken(String refreshToken) {
        return refreshTokenMapper.existsByRefreshToken(refreshToken);
    }

    // 새 Access Token 생성
    private String createNewAccessToken(String refreshToken) {
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        return jwtUtil.createJwt("access", username, role, 10 * 1000L); // 테스트용 짧게 설정 : 10초
    }

    // 새 Refresh Token 생성
    private String createNewRefreshToken(String refreshToken) {
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        return jwtUtil.createJwt("refresh", username, role, 600 * 1000L); // 테스트용 짧게 설정 : 10분
    }

    // Refresh Token DB 저장: 기존 토큰 삭제 후 새 토큰 저장
    private void updateRefreshToken(String oldRefreshToken, String newRefreshToken) {
        refreshTokenMapper.deleteRefreshToken(oldRefreshToken);
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
        refreshTokenDTO.setUsername(jwtUtil.getUsername(newRefreshToken));
        refreshTokenDTO.setRefreshToken(newRefreshToken);
        refreshTokenDTO.setExpiration(jwtUtil.getExpiration(newRefreshToken));
        refreshTokenMapper.insertRefreshToken(refreshTokenDTO);
    }
}
