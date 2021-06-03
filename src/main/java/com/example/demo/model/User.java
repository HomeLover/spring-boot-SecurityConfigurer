package com.example.demo.model;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class User implements UserDetails {
    private Integer id;
    private String username;
    private String password;
    private boolean enabled;
    private boolean locked;
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //获取当前用户对象所具有的用户信息
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for(Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        //获取用户密码
        return password;
    }
    @Override
    public String getUsername() {
        //获取用户姓名
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        //当前账户是否未过期
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        //当前用户是否未锁定
        return !locked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        //当前账户密码是否未过期
        return true;
    }
    @Override
    public boolean isEnabled() {
        //当前用户是否可用
        return enabled;
    }
}
