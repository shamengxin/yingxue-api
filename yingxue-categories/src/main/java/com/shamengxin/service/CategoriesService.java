package com.shamengxin.service;


import com.shamengxin.entity.Category;

import java.util.List;

/**
 * 分类(Category)表服务接口
 *
 * @author makejava
 * @since 2023-09-12 22:24:50
 */
public interface CategoriesService {


    List<Category> findAll();

    Category findById(Integer id);
}
