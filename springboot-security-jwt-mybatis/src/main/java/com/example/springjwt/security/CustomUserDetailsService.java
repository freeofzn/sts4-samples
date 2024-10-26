package com.example.springjwt.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springjwt.mapper.UserMapper;
import com.example.springjwt.model.CustomUserDetails;
import com.example.springjwt.model.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = findUserByUsername(username);
        return new CustomUserDetails(userDTO);
    }

    private UserDTO findUserByUsername(String username) {
        UserDTO userDTO = userMapper.findByUsername(username);
        if (userDTO == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return userDTO;
    }
}
