package com.njs.securityjwt.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.njs.securityjwt.dto.JoinDTO;
import com.njs.securityjwt.entity.UserEntity;
import com.njs.securityjwt.repository.UserRepository;

@Service
public class JoinService {
	private final UserRepository userRepository ;
	private final BCryptPasswordEncoder bCryptPasswordEncoder ;
	
	public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	public void joinProcess(JoinDTO joinDTO) {
		
		String username = joinDTO.getUsername();
		String password = joinDTO.getPassword();	
	
		Boolean isExist = userRepository.existsByUsername(username);
		
		if(isExist) {
			return;
		}
		
		UserEntity data = new UserEntity();
		data.setUsername(username);
		data.setPassword(bCryptPasswordEncoder.encode(password));
		data.setRole("ROLE_ADMIN"); // 일단 AMDIN 권한 하드코딩
		
		System.out.println(data);
		
		userRepository.save(data);
	}	

}
