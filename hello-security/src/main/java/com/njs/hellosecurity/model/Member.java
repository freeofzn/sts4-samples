package com.njs.hellosecurity.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;

@Entity
@Getter
public class Member {
    @Id
    @Column(name = "user_id", nullable = false, unique = true, length = 50)
    private String userId;
    
    @Column(name = "user_pw", nullable = false)
    private String userPw;
    
    @Column(name = "user_name")
    private String userName;
    
    @ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name="member_role",
        joinColumns        = {@JoinColumn(name="user_id")},
        inverseJoinColumns = {@JoinColumn(name="role_cd")}
    )    
    private Set<Role> roles;    
}
