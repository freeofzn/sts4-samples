package com.example.springjwt.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.springjwt.model.RefreshTokenDTO;

@Mapper
public interface RefreshTokenMapper {
    boolean existsByRefreshToken(@Param("refreshToken") String refreshToken);    
    void insertRefreshToken(RefreshTokenDTO refreshTokenDTO);
    void deleteRefreshToken(String refreshToken);    
}