package com.example.springjwt.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjwt.model.JoinDTO;
import com.example.springjwt.service.JoinService;

@RestController
public class JoinController {
	
    private final JoinService joinService;
    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }
 
    @PostMapping("/join")
    public String joinProcess(@RequestBody JoinDTO joinDTO) {    	
        joinService.joinProcess(joinDTO);
        return "ok";
    }
    
}