package com.njs.securityjwt.config;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.njs.securityjwt.jwt.JWTFilter;
import com.njs.securityjwt.jwt.JWTUtil;
import com.njs.securityjwt.jwt.LoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
 	
	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.jwtUtil = jwtUtil;
	}
	
	// AuthenticationManager 등록
    @Bean
    public AuthenticationManager authenticatonManager(AuthenticationConfiguration configuration) throws Exception{
	    return configuration.getAuthenticationManager();
    }    	
	
	// 패스워드 encoder 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
	    return  new BCryptPasswordEncoder();
    }    	
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
	    // csrf disable
	    http.csrf((auth) -> auth.disable());
	    
	    // Form 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());	    
        
	    // http basic basic 인증방식 disable
        http.httpBasic ((auth) -> auth.disable());  
        
	    // API 접근권한 설정
	    http.authorizeHttpRequests((auth) -> auth
	    		    .requestMatchers("/login", "/", "/join", "/users").permitAll() 
                    .requestMatchers("/admin").hasRole("ADMIN")  
                    .anyRequest().authenticated());        
        
	    // JWTFilter 등록(loginFilter 이전필터로 등록)
	    http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
	    
	    // LoginFilter 등록
	    http.addFilterAt(new LoginFilter(authenticatonManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
	    
	    
        // 세션 설정
        http.sessionManagement((session) -> 
        		  session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // JWT 방식에서는 세션을 STATELESS 로 설정!!!
       
    	
        return http.build();
    }

}
