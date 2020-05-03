package com.mmt.user.entity;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.Serializable;

@EntityScan
public class Sys_role implements Serializable {
    private String role_id;
    private String role_name;

    public Sys_role(String role_id){
        this.role_id = role_id;
    }

    public Sys_role(String role_id, String role_name) {
        this.role_id = role_id;
        this.role_name = role_name;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }
}
