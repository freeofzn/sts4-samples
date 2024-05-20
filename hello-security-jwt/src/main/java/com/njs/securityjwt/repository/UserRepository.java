package com.njs.securityjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.njs.securityjwt.entity.UserEntity;
 
public interface UserRepository extends JpaRepository<UserEntity, Long> {
		
	Boolean existsByUsername(String name);
	
	UserEntity findByUsername(String username);
}
