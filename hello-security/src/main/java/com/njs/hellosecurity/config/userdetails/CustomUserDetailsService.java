package com.njs.hellosecurity.config.userdetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.njs.hellosecurity.model.Member;
import com.njs.hellosecurity.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository ;
   
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {    
        Optional<Member> memberOptional = memberRepository.findById(userId);
       
        Member member = memberOptional.orElseThrow(() -> new UsernameNotFoundException("아이디 없음")); // 에러 어디서 받음 ??? -> 학습필요 

        List<GrantedAuthority> authorities = member.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleCd()))
                .collect(Collectors.toList());
        
        /* User 클래스 커스터마이징 하기전: getPrincipal() 로 볼 수 있는 정보가 [id/pass/권한]만 있음
        return new User(
                member.getUserId(),
                member.getUserPw(),
                authorities
        );
        */
        
        // User 클래스 커스터마이징: displayName 추가 -> getPrincipal() 로 볼 수 있는 정보에 displayName 추가됨
        CustomUser customUser = new CustomUser( member.getUserId(), member.getUserPw(), authorities) ;
        customUser.setDisplayName(member.getUserName());
        return customUser;        
        
    }
} 

 