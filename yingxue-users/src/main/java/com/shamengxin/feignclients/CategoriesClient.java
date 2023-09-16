package com.shamengxin.feignclients;

import com.shamengxin.entity.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("API-CATEGORIES")
public interface CategoriesClient {

    @GetMapping("categories/{id}")
    Category findById(@PathVariable("id") Integer id);
}
