package com.njs.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class HelloMavenApplication {
	public static void main(String[] args) {
		SpringApplication.run(HelloMavenApplication.class, args);
	}
	
	@GetMapping(value = "/")
	public String doGetHelloWorld() {
		return "Hello World";
	}

}
