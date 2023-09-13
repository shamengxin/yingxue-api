package com.shamengxin.interceptors;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shamengxin.annotations.RequiredToken;
import com.shamengxin.contants.RedisPre;
import com.shamengxin.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public TokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取当前请求的方法上是否存在RequiredToken
        boolean requiredToken = ((HandlerMethod) handler).getMethod().isAnnotationPresent(RequiredToken.class);
        if (requiredToken) {
            // 1. 获取token信息
            String token = request.getParameter("token");
            log.info("当前传递的token为：{}", token);
            // 2.拼接前缀
            String tokenKey = RedisPre.SESSION + token;
            // 3.在redis中查询
            String userString = stringRedisTemplate.opsForValue().get(tokenKey);
            if (userString == null) {
                throw new RuntimeException("提示：令牌无效，无效token!");
            }
            User user = new ObjectMapper().readValue(userString.getBytes(), User.class);
            // 4.将信息存储到上下文中
            request.setAttribute("token", token);
            request.setAttribute("user", user);
        }
        return true;
    }
}
