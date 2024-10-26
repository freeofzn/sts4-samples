package com.example.springjwt.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TestController {
 
    @GetMapping("/all")
    @ResponseBody
    public Map<String, Object> all() {
    	
    	log.info("/all : 모두 접근가능 ~~~");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "all Controller");
        response.put("message", "모든 사용자(비로그인 포함) 페이지 입니다.");
        response.put("status", "success");
        return response;
    } 
    
    @GetMapping("/loginUserPage")
    public Map<String, Object> loginUserPage() {
    	
        log.info("/loginUserPage : 로그인한 유저 접근가능 ~~~");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "로그인한 사용자 페이지 입니다.");
        response.put("status", "success");
        return response;
    }        
    
    @GetMapping("/admin")
    public Map<String, Object> admin() {
    	
        log.info("/amdin : ADMIN 권한 접근가능 ~~~");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "ROLE_ADMIN 권한 사용자 페이지 입니다.");
        response.put("status", "success");
        return response;
    }    
    
    @GetMapping("/sale")
    public Map<String, Object> sale() {
    	
    	log.info("/sale : SALE 권한 접근가능 ~~~");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "ROLE_SALE 권한 사용자 페이지 입니다.");
        response.put("status", "success");
        return response;
    } 
    
    @GetMapping("/acct")
    public Map<String, Object> acct() {
    	
        log.info("/acct : ACCT 권한 접근가능 ~~~");
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "ROLE_ACCT 권한 사용자 페이지 입니다.");
        response.put("status", "success");
        return response;
    }     
  
}