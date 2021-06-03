package com.example.demo.service;

import com.example.demo.dao.UserDao;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

//public interface UserService {
//    int addUser(User user);
//    User loadUserByUsername(String usermame);
//    List<Role> getUserRolesByUid(Integer id);
//}

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserDao userDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.loadUserByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("账户不存在!");
        } else {
            user.setRoles(userDao.getUserRolesByUid(user.getId()));
            return user;
        }
    }

    public int addUser(User user) {
        return userDao.addUser(user);
    }
}
