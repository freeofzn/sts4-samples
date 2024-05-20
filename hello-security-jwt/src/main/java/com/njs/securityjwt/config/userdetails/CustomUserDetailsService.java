package com.njs.securityjwt.config.userdetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.njs.securityjwt.entity.UserEntity;
import com.njs.securityjwt.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository ;
   
    @Override
//    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {    
        UserEntity userData = userRepository.findByUsername(username);
        
        System.out.println(userData);
        
        //UserEntity user = userOptional.orElseThrow(() -> new UsernameNotFoundException("아이디 없음")); // 에러 어디서 받음 ??? -> 학습필요 
       
        // role 이 N건일때(1유저당 N개의 롤을 부여할때)
        // List<GrantedAuthority> authorities = user.getRoles().stream()
        //   .map(role -> new SimpleGrantedAuthority(role.getRoleCd()))
        //   .collect(Collectors.toList());
        
        
        // role 이 N건일때(1유저당 1개의 롤을 부여할때: 유저테이블에 ROLE을 1개의 컬럼으로 관리할때)
//	    List<GrantedAuthority> authorities = new ArrayList<>();
//	    authorities.add(new SimpleGrantedAuthority(userData.getRole()));
        
        /* User 클래스 커스터마이징 하기전: getPrincipal() 로 볼 수 있는 정보가 [id/pass/권한]만 있음 */
//        return new User(
//        		userData.getUsername(),
//        		userData.getPassword(),
//        		authorities
//        );
       
        // User 클래스 커스터마이징: displayName 추가 어 -> getPrincipal() 로 볼 수 있는 정보에 displayName 추가됨
        return new CustomUserDetails(userData);
        
    }
} 

 