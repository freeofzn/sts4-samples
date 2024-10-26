package com.example.springjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
 
@Controller
public class MainController {
 
    @GetMapping("/")
    String hello(Model model) throws Exception {
    	return "/html/index.html";
    }    
    
}