package com.njs.securityjwt.dto;

import lombok.Setter;
import lombok.Getter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class JoinDTO {
	private String username;
	private String password;

}
