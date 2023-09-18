package com.shamengxin.feignclients;

import com.shamengxin.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("API-USERS")
public interface UsersClient {

    @GetMapping("user/{id}")
    User queryById(@PathVariable("id") Integer id);

}
