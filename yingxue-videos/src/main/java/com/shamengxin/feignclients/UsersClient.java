package com.shamengxin.feignclients;

import com.shamengxin.entity.Favorite;
import com.shamengxin.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("API-USERS")
public interface UsersClient {

    @GetMapping("user/{id}")
    User queryById(@PathVariable("id") Integer id);

    @GetMapping("user/favorite")
    Favorite favorite(@RequestParam("videoId") Integer videoId, @RequestParam("uid") Integer uid);

    @GetMapping("user/comments")
    Map<String,Object> comments(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                @RequestParam(value = "per_page",defaultValue = "5") Integer rows,
                                @PathVariable("videoId") Integer videoId);

}
