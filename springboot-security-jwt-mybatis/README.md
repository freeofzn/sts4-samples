# springboot-security-jwt-mybatis

## 개요

Spring Boot Security 6 JWT Mybatis 
- access token : 로컬 스토리지 저장
- refesh token : 쿠키 와 DB 에 저장 
- access 토큰 만료시 쿠키의 refresh 토큰으로 access/refresh 토큰 재발급
- 로그아웃 클릭시 : refresh 토큰을 DB 와 쿠키에서 삭제 / access 토큰 삭제
- JPA 안쓰고 Mybatis 사용

## 개발환경

- Spring Tool Suite 4 (4.22.0)
- SpringBoot 3.2.5
- SpringSecurity 6
- jsonwebtoken 0.12.3
- JDK17
- Mybatis
- Mysql
- Maven
 
## 설명

Spring Boot Security 6 JWT Mybatis 샘플 간단한 테스트 화면 추가





