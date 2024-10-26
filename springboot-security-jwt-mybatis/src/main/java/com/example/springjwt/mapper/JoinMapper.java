package com.example.springjwt.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.springjwt.model.UserDTO;

@Mapper
public interface JoinMapper {
    boolean existsByUsername(@Param("username") String username);
    void insertUser(UserDTO user);
}