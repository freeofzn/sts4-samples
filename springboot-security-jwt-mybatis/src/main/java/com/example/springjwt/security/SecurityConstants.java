package com.example.springjwt.security;

public class SecurityConstants {
    public static final String[] PERMIT_ALL_URLS = {
        "/", "/login", "/join", "/all", "/reissue"  // 인증없이 사용가능한 URL 
    };
}
 