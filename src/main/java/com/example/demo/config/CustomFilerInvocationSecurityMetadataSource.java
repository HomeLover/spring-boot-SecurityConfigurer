package com.example.demo.config;

import com.alibaba.fastjson.JSON;
import com.example.demo.dao.MenuMapper;
import com.example.demo.model.Menu;
import com.example.demo.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * @2021.06.13 18:37 FilterInvocationSecurityMetadataSource重写
 */

@Component
public class CustomFilerInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private MenuMapper menuMapper;
    private static List<Menu> allMenus = null;

    /**
     * 获取所有的资源信息
     * 在getAttrbutes()方法内不可直接使用menuMapper,因为Security会优先于Spring加载,此时还没有注入,会报空指针异常
     * 被@PostConstrust修饰的方法会在服务器加载Servlet的时候运行,并且只会被服务器运行一次,依赖注入初始化后会自动执行该方法
     */
    @PostConstruct
    private List<Menu> getMenus() {
        if (null == allMenus) {
            System.out.println("-----menuMapper-----> " + menuMapper);
            allMenus = menuMapper.getAllMenus();
            System.out.println("-----allMenus-----> \n" + JSON.toJSONString(allMenus));
        }
        return allMenus;
    }

    /**
     * 通过当前请求的URL获取角色信息
     *
     * @param o(FilterInvocation) 用于获取当前请求的URL
     * @return 当前请求URL所需的角色
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        System.out.println("*****************************************************************");
        System.out.println("----------------> CustomerFilterInvocationSecurityMetadataSource.getAttributes(Object)");
        String url = ((FilterInvocation) o).getRequestUrl();
        System.out.println("-----url-----> " + url);
        for (Menu menu : allMenus) {
            if (antPathMatcher.match(menu.getPattern(), url)) {
                List<Role> roles = menu.getRoles();
                String[] roleArr = new String[roles.size()];
                for (int i = 0; i < roleArr.length; i++) {
                    roleArr[i] = roles.get(i).getName();
                }
                return SecurityConfig.createList(roleArr);
            }
        }
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    /**
     * 返回所有定义好的权限资源
     * Spring Security 在启动时会校验相关配置是否正确,如果不需要校验,则返回null
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * 是否支持校验
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
