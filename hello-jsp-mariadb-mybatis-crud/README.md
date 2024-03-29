# hello-jsp

## 개요

SpringBoot JSP mariadb mybatis crud 게시판 샘플

## 개발환경

- Spring Tool Suite 4 (4.22.0)
- SpringBoot 3.2.3
- JDK17
- Maven

## 설명

- SpringBoot JSP mariadb mybatis crud 게시판 샘플


## 테스트용 테이블 생성 sql

```
CREATE TABLE BOARD(  
    NUM INT NOT NULL AUTO_INCREMENT,  
    NAME       VARCHAR(100) NOT NULL,  
    PWD        VARCHAR(100),
    EMAIL      VARCHAR(100),
    SUBJECT    VARCHAR(100),
    CONTENT    VARCHAR(100),
    IP_ADDR    VARCHAR(100),
    HIT_COUNT  VARCHAR(100),
    CREATED    VARCHAR(100),	 	 	 	     
    PRIMARY KEY ( NUM )
)
```