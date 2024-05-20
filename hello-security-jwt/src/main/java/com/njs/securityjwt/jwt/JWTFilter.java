package com.njs.securityjwt.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.njs.securityjwt.config.userdetails.CustomUserDetails;
import com.njs.securityjwt.entity.UserEntity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {
	private final JWTUtil jwtUtil;
	
	public JWTFilter(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	// 토큰 검증
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
		
        final String authorizationHeader = request.getHeader("Authorization");

    	// Authorization 헤더 검증
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
       	    System.out.println("token null");
       	    filterChain.doFilter(request, response);

       	    // 조건이 해당되면 메소드 종료 (필수)
       	    return;
        }
        
        String token = authorizationHeader.split(" ")[1];
        
        // 토큰 소멸 시간 검증
        if( jwtUtil.isExpired(token)) {
            System.out.println("token expired");
          	filterChain.doFilter(request, response);        	

       	    // 조건이 해당되면 메소드 종료 (필수)
       	    return;
        }

        // 토큰에서 username 과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        
        // userEntity 를 생성하여 값 set
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword("temppassword"); // 임시패스워드로 셋팅
        userEntity.setRole(role);
        
        // UserDetails 에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
        
        // 스프링 시큐리티 인증토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        
        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken); 
        
        filterChain.doFilter(request, response);
    } 
}
