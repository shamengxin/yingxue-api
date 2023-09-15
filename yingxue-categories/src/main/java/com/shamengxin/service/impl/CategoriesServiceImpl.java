package com.shamengxin.service.impl;


import com.shamengxin.entity.Category;
import com.shamengxin.mapper.CategoriesMapper;
import com.shamengxin.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 分类(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-09-12 22:24:50
 */
@Service("categoryService")
public class CategoriesServiceImpl implements CategoriesService {

    private CategoriesMapper categoriesMapper;

    @Autowired
    public CategoriesServiceImpl(CategoriesMapper categoriesMapper) {
        this.categoriesMapper = categoriesMapper;
    }

    @Override
    public List<Category> findAll() {
       return categoriesMapper.findAll();

    }

    @Override
    public Category findById(Integer id) {
        return categoriesMapper.findById(id);
    }
}
