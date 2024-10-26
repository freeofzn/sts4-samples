package com.example.springjwt.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springjwt.mapper.JoinMapper;
import com.example.springjwt.model.JoinDTO;
import com.example.springjwt.model.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JoinService {
    
    private final JoinMapper joinMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(JoinMapper joinMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.joinMapper = joinMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String role     = joinDTO.getRole();

        Boolean isExist = joinMapper.existsByUsername(joinDTO.getUsername());
        if (isExist) {
        	log.info("아이디 중복: {}", username );
            return;
        }

        UserDTO data = new UserDTO();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole(role);
        
        joinMapper.insertUser(data);
        
    }
    
}
