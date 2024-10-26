package com.example.springjwt.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString 
public class RefreshTokenDTO {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("USERNAME")
    private String username;

    @JsonProperty("REFRESH_TOKEN")
    private String refreshToken;

    @JsonProperty("EXPIRATION")
    private LocalDateTime expiration;
}
