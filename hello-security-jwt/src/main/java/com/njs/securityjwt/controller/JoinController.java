package com.njs.securityjwt.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.njs.securityjwt.dto.JoinDTO;
import com.njs.securityjwt.repository.UserRepository;
import com.njs.securityjwt.service.JoinService;

import lombok.RequiredArgsConstructor;

@Controller
@ResponseBody
public class JoinController {
	
	private final JoinService joinService;
	
	public JoinController(JoinService joinService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.joinService = joinService;
	}	
	
	@PostMapping("/join")
	public String joinProcess(JoinDTO joinDTO) { // @RequestBody : JSON 으로 받을때 붙여주기
		
		System.out.println("controller:" + joinDTO);
		
		joinService.joinProcess(joinDTO);
		
		return joinDTO.toString();
	}
		
}