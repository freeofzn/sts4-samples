package com.njs.hellosecurity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Role {
    @Id
    @Column(name = "role_cd", nullable = false, length = 20) 
    private String roleCd;
    
    @Column(name = "role_nm", nullable = false, length = 128) 
    private String roleNm;
    
    @Column(name = "description", length = 4000) 
    private String description;
    
}
