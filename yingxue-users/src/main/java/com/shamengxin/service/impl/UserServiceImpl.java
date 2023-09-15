package com.shamengxin.service.impl;

import com.shamengxin.entity.User;
import com.shamengxin.mapper.UserMapper;
import com.shamengxin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户(User)表服务实现类
 *
 * @author makejava
 * @since 2023-09-12 22:46:05
 */
@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper usermapper) {
        this.userMapper = usermapper;
    }

    @Override
    public User findPhone(String phone) {
        return userMapper.findPhone(phone);

    }

    @Override
    public User insert(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    public void update(User user) {
        user.setUpdatedAt(new Date());
        userMapper.update(user);
    }
}
