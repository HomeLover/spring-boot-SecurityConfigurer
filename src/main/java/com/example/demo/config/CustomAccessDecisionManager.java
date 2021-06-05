package com.example.demo.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication,
                       Object object,
                       Collection<ConfigAttribute> configAttributes) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for(ConfigAttribute configAttribute : configAttributes) {
            if("ROLE_LOGIN".equals(configAttribute.getAttribute()) && authorities instanceof UsernamePasswordAuthenticationToken) {
                return;
            }
            for (GrantedAuthority authority : authorities) {
                if(configAttribute.getAttribute().equals(authority.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足");
    }
    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
