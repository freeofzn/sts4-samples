package com.njs.securityjwt.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.njs.securityjwt.entity.UserEntity;
import com.njs.securityjwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@ResponseBody
public class MainController {
	private final UserRepository userRepository ;
	
	@GetMapping("/")
	public String main() {
		
		return "main controller";
	}
		
 
	@GetMapping("/users")
	String getAllUsers( ) {
        List<UserEntity> users = userRepository.findAll();
		return users.toString();
		
	}			
	
}