package com.njs.hellosecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	// 모든유저 
	@GetMapping("/allUser")
	String allUserForm() {
		return "pages/all-user-page.html";
	}		
	
	// 로그인유저 
	@GetMapping("/loginUser")
	String loginUserForm( ) {
		return "pages/login-user-page.html";
	}		
	
	// 재무
	@GetMapping("/acct")
	String acctForm(Authentication auth) {
		return "pages/acct-page.html";
	}		
	
	// 영업
	@GetMapping("/sale")
	String saleForm() {
		return "pages/sale-page.html";
	}	
	
	// 관리자
	@GetMapping("/admin")
	String adminForm(Authentication auth) {
		return "pages/admin-page.html";
	}	
	
    // 로그인
	@GetMapping({"/", "/login"})
	String loginForm(Authentication auth) {
		if(auth != null && auth.isAuthenticated()) {
			return "redirect:/loginUser";  // 기 로그인 유저가 /login 접근시 -> /loginUser 페이지로 이동
		} 
		return "login.html";
	}	
	
}