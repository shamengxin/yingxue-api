package com.shamengxin.controller;

import com.shamengxin.entity.User;
import com.shamengxin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserServiceController {

    private UserService userService;

    public UserServiceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/{id}")
    public User queryById(@PathVariable("id") Integer id){
        log.info("接收到的用户id：{}",id);
        User user = userService.queryById(id);
        log.info("获取到的用户信息为：{}",user);
        return user;
    }
}
