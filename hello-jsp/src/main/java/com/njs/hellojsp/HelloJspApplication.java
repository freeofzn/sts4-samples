package com.njs.hellojsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class HelloJspApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloJspApplication.class, args);
	}
	
	@GetMapping(value = "/")
	public String doGetHelloWorld() {
		 return "hello world";
	}	

}
