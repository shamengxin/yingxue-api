package com.shamengxin.controller;


import com.shamengxin.entity.Category;
import com.shamengxin.service.CategoriesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 分类(Category)表控制层
 *
 * @author makejava
 * @since 2023-09-12 22:24:50
 */
@RestController
@Slf4j
public class CategoriesController {
    private CategoriesService categoriesService;

    @Autowired
    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("categories/{id}")
    public Category findById(@PathVariable("id") Integer id){
        log.info("接收到的类别id：{}",id);
        return categoriesService.findById(id);
    }

    /**
     * 分类列表
     *
     * @return
     */
    @GetMapping("categories")
    public List<Category> findAll() {
        List<Category> categories = categoriesService.findAll();
        log.info("查询的一级类别的列表总数为:{}", categories.size());
        return categories;
    }

}

