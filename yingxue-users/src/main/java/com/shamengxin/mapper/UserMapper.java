package com.shamengxin.mapper;

import com.shamengxin.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户(User)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-12 22:46:04
 */
@Mapper
public interface UserMapper {


    User findPhone(String phone);

    void insert(User user);

    void update(User user);

    User queryById(Integer id);
}

