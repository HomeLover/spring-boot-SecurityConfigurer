package com.example.demo.dao;

import com.example.demo.model.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<Menu> getAllMenus();
}
