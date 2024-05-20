package com.njs.securityjwt.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.njs.securityjwt.config.userdetails.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    
    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

   // 인증
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        try {
	    	// 요청에서 유저 정보를 가져오기: form-data 요청시
	        String username = obtainUsername(request);
	        String password = obtainPassword(request);
	        
	    	// 요청에서 유저 정보를 가져오기: JSON 요청시
//	    	ObjectMapper objectMapper = new ObjectMapper();
//	        JsonNode jsonNode = objectMapper.readTree(request.getInputStream());
//	        String username = jsonNode.get("username").asText();
//	        String password = jsonNode.get("password").asText();        
	        
	        System.out.println(username);
	        System.out.println(password);
	
	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
	        
	        // 가져온 유저 정보로 Authentication 객체 생성
	        return authenticationManager.authenticate(authToken);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to parse login request", e);
//        }        
    }

	@Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        // 인증이 성공하면 이 메서드가 호출됨
        // JWT 토큰 생성 등의 작업을 수행할 수 있음
		
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

		String username = customUserDetails.getUsername();

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
		GrantedAuthority auth = iterator.next();
		String role = auth.getAuthority();		
		
		System.out.println("로그인 성공:" + username + "/" + role);
		
		String token = jwtUtil.createJwt(username, role, 60 * 60 * 1000L); 
		
		response.addHeader("Authorization", "Bearer " + token);
		// JWT 발급
		// JWTUtil 
    }
	
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        // 인증이 실패하면 이 메서드가 호출됨
        // 실패에 대한 응답을 처리할 수 있음
    	
    	System.out.println("로그인 실패");
    	response.setStatus(401);
    	
        // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        // response.getWriter().write("Authentication failed: " + failed.getMessage());
    }
}