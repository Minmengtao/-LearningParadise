package com.mmt.user.repository;

import java.util.List;

import com.mmt.user.entity.Sys_role;
import com.mmt.user.entity.Sys_user_role;
import com.mmt.user.entity.Sys_user;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * mapper的具体表达式
 */
@Mapper //标记mapper文件位置，否则在Application.class启动类上配置mapper包扫描
//@Repository
public interface UserMapper {

    /**
     * 查询用户名是否存在，若存在，不允许注册
     * 注解@Param(value) 若value与可变参数相同，注解可省略
     * 注解@Results  列名和字段名相同，注解可省略
     * @param user_name
     * @return
     */
    @Select("select * from user u where u.user_name=#{user_name}")
    public Sys_user findUserByName(@Param("user_name") String user_name);

    /**
     * 注册新用户
     * @param user_id
     * @param user_name
     * @param user_pwd
     */
    @Insert("insert into user (user_id,user_name,user_pwd) values(#{user_id},#{user_name},#{user_pwd})")
    //加入该注解可以保存对象后，查看对象插入id
    //@Options(useGeneratedKeys = true,keyProperty = "user_id",keyColumn = "user_id")
    public void regist(@Param("user_id") String user_id,@Param("user_name") String user_name,@Param("user_pwd") String user_pwd);

    /**
     * 更新用户所有记录
     */
    @Update("update user set user_name=#{user_name}, user_pwd=#{user_pwd}, user_sex=#{user_sex}, " +
            "user_email=#{user_email}, user_phone=#{user_photo}, user_photo=#{user_photo} where user_id = #{user_id}")
    public void updateUser(@Param("user_name") String user_name,@Param("user_pwd") String user_pwd,@Param("user_sex") String user_sex
            ,@Param("user_email") String user_email,@Param("user_photo") String user_photo,@Param("user_id") String user_id);

    /**
     * 删除id为user_id的用户所有信息
     * @param user_id
     */
    @Delete("delete from user where user_id = #{user_id}")
    public void deleteUser(@Param("user_id") String user_id);

    @Insert("insert into sys_user_role (user_id, role_id) values(#{user_id}, #{role_id})")
    void insertSysUserRole(@Param("user_id") String user_id, @Param("role_id") String role_id);

    @Select("select user_id,role_id from sys_user_role r where r.user_id = #{user_id}")
    List<Sys_user_role> queryRole(@Param("user_id") String user_id);

    @Delete("delete from sys_user_role where user_id = #{user_id}")
    public void deleteSysUserRole(@Param("user_id") String user_id);

    @Select("select * from sys_role r where r.role_id = #{role_id}")
    public Sys_role SelectRolebyId(@Param("role_id") String role_id);

}
