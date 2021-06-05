package com.example.demo.dao;

import com.example.demo.model.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuDao {
    @Select("SELECT m.*,r.id AS rid,r.name AS rname,r.nameZh AS rnameZh FROM menu m LEFT JOIN menu_role mr ON m.`id`=mr.`mid` LEFT JOIN role r ON mr.`rid`=r.`id`")
    List<Menu> getAllMenus();
}
