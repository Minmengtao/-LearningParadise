package com.mmt.user.security.service;

import com.mmt.user.entity.Sys_role;
import com.mmt.user.entity.Sys_user_role;
import com.mmt.user.entity.Sys_user;
import com.mmt.user.service.UserServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserServiceImp imp;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 从数据库中取出用户信息
        System.out.println("当前CustomUser的登录用户为" + username);
        Sys_user user = imp.findUserByName(username);
        // 判断用户是否存在
        if(user == null)
            throw new UsernameNotFoundException("用户名不存在");
        System.out.println("password="+user.getUser_pwd());
        // 添加权限
        List<Sys_user_role> userRoles = imp.queryRole(user.getUser_id());
        System.out.println("userRoles的长度为"+userRoles.size()+",第一个角色主键为"+userRoles.get(0).getRole_id()
            +",用户主键为"+userRoles.get(0).getUser_id());
        for (Sys_user_role userRole : userRoles) {
            Sys_role role = imp.SelectRolebyId(userRole.getRole_id());
            authorities.add(new SimpleGrantedAuthority(role.getRole_name()));
        }
        return new User(user.getUser_id(), user.getUser_pwd(), authorities);
    }
}
