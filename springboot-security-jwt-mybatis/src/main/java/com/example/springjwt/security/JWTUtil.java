package com.example.springjwt.security;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${my.jwt.secret}")String secret) {
    	System.out.println("JWTUtil: my.jwt.secret="+ secret);
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 토큰검증 username
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    // 토큰검증 role    
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }
    
    // 토큰검증 category     
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }    
    
    // 토큰에서 만료일자 추출
    public LocalDateTime getExpiration(String token) {
        try {
            // 토큰을 파싱하고 만료일자를 추출
            Date expirationDate = Jwts.parser() 
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token) 
                .getPayload()
                .getExpiration();  // 만료일자 추출

            // Date 타입을 LocalDateTime으로 변환
            return expirationDate.toInstant()
                                 .atZone(ZoneId.systemDefault())
                                 .toLocalDateTime();

        } catch (Exception e) {
            return null;  // 유효하지 않거나 만료된 토큰일 경우 null 반환
        }
    }

    // 토큰검증 만료여부    
    public Boolean isExpired(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // 만료된 토큰일 경우 true 반환
        }        
    }

    // 토큰발급
    public String createJwt(String category, String username, String role, Long expiredMs) {
        return Jwts.builder()
        		.claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
