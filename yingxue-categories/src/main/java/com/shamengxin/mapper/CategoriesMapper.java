package com.shamengxin.mapper;


import com.shamengxin.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 分类(Category)表数据库访问层
 *
 * @author makejava
 * @since 2023-09-12 22:24:50
 */
@Mapper
public interface CategoriesMapper {


    List<Category> findAll();

    Category findById(Integer id);
}

