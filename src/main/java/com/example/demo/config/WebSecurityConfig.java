package com.example.demo.config;

import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;
    @Bean
    BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    //授权,放行adduser路由
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userService);
    }
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(new CustomFilerInvocationSecurityMetadataSource());
                        object.setAccessDecisionManager(new CustomAccessDecisionManager());
                        return object;
                    }
                })
                .and()
                // 开启表单登录,即登录页面
                .formLogin()
                // 自定义登录页,未配置下启用默认的登录页
                // .loginPage("/login_page")
                // 登录请求接口,默认为 "/login"接口
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request,response,authentication) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    PrintWriter out = response.getWriter();
                    response.setStatus(HttpServletResponse.SC_OK);
                    Map<String,Object> map = new HashMap<>();
                    map.put("status",HttpServletResponse.SC_OK);
                    map.put("msg", authentication.getPrincipal());
                    ObjectMapper om = new ObjectMapper();
                    out.write(om.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .failureHandler((httpServletRequest, httpServletResponse, e) -> {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    PrintWriter out = httpServletResponse.getWriter();
                    httpServletResponse.setStatus(401);
                    Map<String,Object> map = new HashMap<>();
                    map.put("status",401);
                    if(e instanceof LockedException) {
                        map.put("msg","账号已被锁定,登录失败!");
                    } else if(e instanceof BadCredentialsException) {
                        map.put("msg","账户名或密码输入错误，登录失败！");
                    } else if(e instanceof DisabledException) {
                        map.put("msg","账号被禁用,登录失败!");
                    } else if(e instanceof AccountExpiredException) {
                        map.put("msg","账号已过期,登录失败!");
                    } else if(e instanceof CredentialsExpiredException) {
                        map.put("msg","密码已过期");
                    } else {
                        map.put("msg","登录失败,账号不存在!");
                    }
                    ObjectMapper om = new ObjectMapper();
                    out.write(om.writeValueAsString(map));
                    out.flush();
                    out.close();
                })
                .permitAll()
                .and()
                .csrf().disable();
    }
//    @Bean
//    public RoleHierarchyVoter roleHierarchyVoter() {
//        return new RoleHierarchyVoter(roleHierarchy());
//    }
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        String hierarchy = "ROLE_dba > ROLE_admin \n ROLE_admin > ROLE_user";
//        roleHierarchy.setHierarchy(hierarchy);
//        return roleHierarchy;
//    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(){});
        return provider;
    }

    /**
     * 自定义方法 获取Cookie信息
     */
    private void getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
    }
}