package com.mmt.user.service;

import java.util.List;
import java.util.UUID;

import com.mmt.user.entity.Sys_role;
import com.mmt.user.entity.Sys_user_role;
import com.mmt.user.entity.Sys_user;
import com.mmt.user.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserMapper userMapper;
    /*
    按名字查找账号
     */
    @Override
    public Sys_user findUserByName(String user_name) {
        return userMapper.findUserByName(user_name);
    }
    /*
    注册
     */
    @Override
    public void regist(String user_name, String user_pwd) {
        String user_id,s=UUID.randomUUID().toString();
        //自动生成用户id（而且不重复）
        user_id = "用户" + s.substring(0, 8) + s.substring(9, 13);
        System.out.println(user_id);
        userMapper.regist(user_id, user_name, user_pwd);
        insertSysUserRole(user_id, "2");//更新用户权限
    }

    @Override
    public void updateUser(String user_name, String user_pwd, String user_sex, String user_email, String user_photo, String user_id) {
        userMapper.updateUser(user_name, user_pwd, user_sex, user_email, user_photo, user_id);
    }

    @Override
    public void deleteUser(String user_id) {
        deleteSysUserRole(user_id);//删除用户时需要将所有有关的数据均要删除
        userMapper.deleteUser(user_id);
    }

    @Override
    public void insertSysUserRole(String user_id, String role_id) {
        userMapper.insertSysUserRole(user_id, role_id);
    }

    @Override
    public List<Sys_user_role> queryRole(String user_id) {
        return userMapper.queryRole(user_id);
    }

    @Override
    public void deleteSysUserRole(String user_id) {
        userMapper.deleteSysUserRole(user_id);
    }

    @Override
    public Sys_role SelectRolebyId(String role_id){
        return userMapper.SelectRolebyId(role_id);
    }
}
