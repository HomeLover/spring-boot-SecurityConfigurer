package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/adduser")
    public Object addUser(@RequestParam (value = "username")String username,
                          @RequestParam (value = "password")String password)
    {
        Map<String,Object>map = new HashMap<>();
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setEnabled(true);
        user.setLocked(false);
        int addCode = userService.addUser(user);
        if(addCode == 1) {
            map.put("code",200);
            map.put("msg","添加用户成功");
        } else {
            map.put("code",400);
            map.put("msg","添加失败");
        }
        return map;
    }
}
