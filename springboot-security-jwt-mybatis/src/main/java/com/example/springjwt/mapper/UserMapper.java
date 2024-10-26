package com.example.springjwt.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.springjwt.model.UserDTO;

@Mapper
public interface UserMapper {
    UserDTO findByUsername(@Param("username") String username);    
}