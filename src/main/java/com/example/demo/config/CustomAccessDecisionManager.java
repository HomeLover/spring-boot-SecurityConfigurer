package com.example.demo.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @datime 2021/06/13 重写 AccessDecisionManager
 * @Author wylyounng
 */
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {
    /**
     * 判断当前登录的用户是否具备当前请求URL所需要的角色
     * 如果不具备就抛出 AccessDeniedException异常,否则不做任何事情即可
     * @param authentication 当前登录用户的信息
     * @param object FilterInvocation对象,可以用来获取当前的请求对象
     * @param collection FilterInvocationSecurityMetadtaSource中的 getAttributes() 方法的返回值()即当前请求URL所需要的角色
     */
    @Override
    public void decide(Authentication authentication,Object object,Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for(ConfigAttribute configAttribute : collection) {
            /**
             * 如果需要的角色是ROLE_ANONYMOUS,说明当前请求的URL用户登录后即可访问
             * 如果authentication是UsernamePassordAuthenticationToken实例,则说明该用户已经登录了
             */
            if("ROLE_ANONYMOUS".equals(configAttribute.getAttribute())  && authentication instanceof UsernamePasswordAuthenticationToken) {
                return;
            }
            for(GrantedAuthority authority : authorities) {
                if(configAttribute.getAttribute().equals(authority.getAuthority())) {
                    return;
                }
            }
            throw new AccessDeniedException("权限不足");
        }
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
