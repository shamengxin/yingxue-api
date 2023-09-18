package com.shamengxin.service;

import com.shamengxin.entity.User;

/**
 * 用户(User)表服务接口
 *
 * @author makejava
 * @since 2023-09-12 22:46:05
 */
public interface UserService {


    User findPhone(String phone);

    User insert(User user);

    void update(User user);

    User queryById(Integer id);
}
