package com.example.demo.dao;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDao {
    @Insert("insert into user (username,password) values (#{username},#{password})")
    int addUser(User user);
    @Select("select * from user where username = #{username}")
    User loadUserByUsername(String username);
    @Select("select * from role r,user_role ur where r.id = ur.uid and ur.uid = #{id}")
    List<Role> getUserRolesByUid(Integer id);
}
