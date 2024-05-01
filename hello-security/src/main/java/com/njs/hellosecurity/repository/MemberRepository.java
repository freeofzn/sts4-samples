package com.njs.hellosecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.njs.hellosecurity.model.Member;
 
public interface MemberRepository extends JpaRepository<Member, String> {
		
}
