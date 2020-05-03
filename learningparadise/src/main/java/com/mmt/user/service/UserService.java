package com.mmt.user.service;

import com.mmt.user.entity.Sys_role;
import com.mmt.user.entity.Sys_user_role;
import com.mmt.user.entity.Sys_user;

import java.util.List;

public interface UserService {
    public Sys_user findUserByName(String user_name);

    public void regist(String user_name, String user_pwd);
    public void updateUser(String user_name, String user_pwd, String user_sex
            , String user_email, String user_photo, String user_id);
    public void deleteUser(String user_id);
    void insertSysUserRole(String user_id, String role_id);
    List<Sys_user_role> queryRole(String user_id);
    public void deleteSysUserRole(String user_id);
    public Sys_role SelectRolebyId(String role_id);

}
