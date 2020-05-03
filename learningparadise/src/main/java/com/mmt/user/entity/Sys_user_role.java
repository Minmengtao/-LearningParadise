package com.mmt.user.entity;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.io.Serializable;

@EntityScan
public class Sys_user_role implements Serializable {
    private String user_id;
    private String role_id;

    public Sys_user_role(String user_id, String role_id) {
        this.user_id = user_id;
        this.role_id = role_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }


}
