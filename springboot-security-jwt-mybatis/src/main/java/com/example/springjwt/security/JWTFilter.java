package com.example.springjwt.security;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.springjwt.model.CustomUserDetails;
import com.example.springjwt.model.UserDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    // JWT 유틸리티 클래스 의존성 주입
    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Authorization 헤더에서 JWT 토큰 추출
        String authorization = request.getHeader("Authorization");
        
        // 2. 인증이 필요 없는 경로 설정 (PERMIT_ALL_URLS에 포함된 경우 필터링 우회)
        String requestURI = request.getRequestURI();
        if (Arrays.asList(SecurityConstants.PERMIT_ALL_URLS).contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }        

        // 3. 헤더가 비어있거나 Bearer 형식이 아니면 필터 체인 계속 실행 후 종료
        if (authorization == null || !authorization.startsWith("Bearer ")) {
        	log.info("JWTFilter: access token 없음 {}");
            filterChain.doFilter(request, response);
            return;
        }

        // 4. "Bearer " 접두사 제거 후 토큰 추출
        String accessToken = authorization.split(" ")[1];

        // 5. 토큰이 없으면 필터 체인 계속 실행 후 종료
        if (accessToken == null) {
            filterChain.doFilter(request, response); // 다음 필터로 넘김 !!!
            return;
        }

        // 6. 토큰 만료 여부 확인, 만료 시 응답 처리 후 종료(다음 필터로 넘기지 않음 !!!)
        if (jwtUtil.isExpired(accessToken)) {
        	log.info("access 토큰 만료됨!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        // 7. 토큰 카테고리가 "access"인지 확인
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 8. 토큰에서 username과 role 추출
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        // 9. UserDTO 객체 생성 및 사용자 정보 설정
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword("temppassword"); // 실제 비밀번호는 사용되지 않음
        userDTO.setRole(role);

        // 10. CustomUserDetails 객체 생성 및 사용자 정보 설정
        CustomUserDetails customUserDetails = new CustomUserDetails(userDTO);

        // 11. Authentication 토큰 생성 (username, password, 권한)
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 12. SecurityContext에 인증 정보 설정 (세션에 사용자 등록)
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 13. 필터 체인 계속 실행
        filterChain.doFilter(request, response);
    }
}
