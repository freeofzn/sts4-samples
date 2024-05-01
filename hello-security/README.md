# hello-security

## 개요

Spring Boot Security 6 / Thymeleaf / mariadb JPA 샘플

## 개발환경

- Spring Tool Suite 4 (4.22.0)
- SpringBoot 3.2.5
- SpringSecurity 6
- JDK17
- JPA
- MariaDB
- Maven

## 설명

- Spring Boot Security 6(세션사용/JWT아님) / Thymeleaf / mariadb JPA 샘플

## 테스트용 테이블: JPA 자동생성

## 테스트용 데이타: 비번 = 123

```
INSERT INTO `member` (`user_id`, `user_name`, `user_pw`) VALUES ('acct', '재무', '$2a$10$1qYHra2V/BTb3Vr0VpYXVemne5BZm/1.HYMwQ/vzZRXRrQDbI4LTu');
INSERT INTO `member` (`user_id`, `user_name`, `user_pw`) VALUES ('admin', '관리자', '$2a$10$NEMMJtjaleIDYf4N9fYBXujivUGxVrsnuIowB9nk41Zjj/unn.Biu');
INSERT INTO `member` (`user_id`, `user_name`, `user_pw`) VALUES ('prod', '제조', '$2a$10$IaLsuJQGk0EeNl1QisDXl.4NjcsKlJTQ7Qjg.UfrGHdLp5iwqP9kK');
INSERT INTO `member` (`user_id`, `user_name`, `user_pw`) VALUES ('sale', '영업', '$2a$10$NEMMJtjaleIDYf4N9fYBXujivUGxVrsnuIowB9nk41Zjj/unn.Biu');

INSERT INTO `role` (`role_cd`, `description`, `role_nm`) VALUES ('ADMIN', '관리자', '관리자');
INSERT INTO `role` (`role_cd`, `description`, `role_nm`) VALUES ('USER_ACCT', '재무유저', '재무유저');
INSERT INTO `role` (`role_cd`, `description`, `role_nm`) VALUES ('USER_PROD', '제조유저', '제조유저');
INSERT INTO `role` (`role_cd`, `description`, `role_nm`) VALUES ('USER_SALE', '영업유저', '영업유저');

INSERT INTO `member_role` (`user_id`, `role_cd`) VALUES ('admin', 'USER_ACCT');
INSERT INTO `member_role` (`user_id`, `role_cd`) VALUES ('acct', 'USER_ACCT');
INSERT INTO `member_role` (`user_id`, `role_cd`) VALUES ('sale', 'USER_SALE');
INSERT INTO `member_role` (`user_id`, `role_cd`) VALUES ('prod', 'USER_PROD');
INSERT INTO `member_role` (`user_id`, `role_cd`) VALUES ('admin', 'USER_SALE');
INSERT INTO `member_role` (`user_id`, `role_cd`) VALUES ('admin', 'ADMIN');
INSERT INTO `member_role` (`user_id`, `role_cd`) VALUES ('admin', 'USER_PROD');
```
