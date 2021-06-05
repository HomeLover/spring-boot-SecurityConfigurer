package com.example.demo.config;

import com.example.demo.dao.MenuDao;
import com.example.demo.model.Menu;
import com.example.demo.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

@Component
public class CustomFilerInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Autowired
    MenuDao menuDao;
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<Menu> allMenus = menuDao.getAllMenus();
        System.out.println(allMenus);
        for(Menu menu : allMenus) {
            if(antPathMatcher.match(menu.getPattern(),requestUrl)) {
                List<Role> roles = menu.getRoles();
                String[] roleArr = new String[roles.size()];
                System.out.println(roles);
                for(int i = 0;i < roleArr.length; i++) {
                    roleArr[i] = roles.get(i).getName();
                    System.out.println(roles.get(i).getName());
                }
                return SecurityConfig.createList(roleArr);
            }
        }
        return SecurityConfig.createList("ROLE_LOGIN");
    }
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
