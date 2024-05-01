package com.njs.hellosecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize 사용하기 위하여 추가 ( default true 라서 추가할 필요 없다고 하는데 추가 안하니 작동을 안해서 추가함)
public class SecurityConfig {

	// 패스워드 인코더 등록
    @Bean
    PasswordEncoder passwordEncoder() {
	    return  new BCryptPasswordEncoder();
    }
 
    // CSRF 
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }
	
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	 
	    // CSRF 체크 OFF
//	    http.csrf((csrf) -> csrf.disable());
	    
    	// CSRF 체크 ON 
	    http.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository())
   	    	               	   // csrf 예외처리(FORM : POST 전송해야 적용됨)
                               // .ignoringRequestMatchers("/login")  
	   	                       // .ignoringRequestMatchers("/logout") 
	   	);	   

	    // API 접근권한 설정
	    http.authorizeHttpRequests((authorize) ->
                 authorize.requestMatchers("/admin").hasAuthority("ADMIN")
                          .requestMatchers("/acct").hasAnyAuthority("ADMIN", "USER_ACCT")
                          .requestMatchers("/sale").hasAnyAuthority("ADMIN", "USER_SALE")
                          .requestMatchers("/loginUser").authenticated()
                          .requestMatchers("/allUser").permitAll()
                          .requestMatchers("/**").permitAll()
        );
	    
	    // 폼으로 로그인 하겠다는 설정(form POST 로 요청필요)
        http.formLogin((formLogin) -> formLogin.loginPage("/login")
    		 .defaultSuccessUrl("/loginUser"));
             // 실패시 디플트로 /login?error 로 이동함
    		 //.failureUrl("/fail"));
     
        // 로그아웃 URL 설정 -> csrf 설정하면 FORM POST 로 요청해야 됨(예외시에도 FORM POST 필요)
        http.logout(logout -> logout.logoutUrl("/logout"));
	          
        return http.build();
    }

}