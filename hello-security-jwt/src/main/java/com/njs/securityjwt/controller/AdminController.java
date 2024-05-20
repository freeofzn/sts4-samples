package com.njs.securityjwt.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AdminController {
	
	@GetMapping("/admin")
	public String admin() {
		
		System.out.println("어드민 컨트롤러 입니다!!!");
		
		return "admin controller";
	}
	
 
	
}